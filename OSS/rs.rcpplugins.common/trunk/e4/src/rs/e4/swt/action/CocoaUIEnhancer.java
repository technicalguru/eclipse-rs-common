package rs.e4.swt.action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;

import rs.baselib.lang.LangUtils;

/**
 * Provide a hook to connecting the Preferences, About and Quit menu items of the Mac OS X
 * Application menu when using the SWT Cocoa bindings.
 * <p>
 * This code does not require the Cocoa SWT JAR in order to be compiled as it uses reflection to
 * access the Cocoa specific API methods. It does, however, depend on JFace (for IAction), but you
 * could easily modify the code to use SWT Listeners instead in order to use this class in SWT only
 * applications.
 * </p>
 * <p>
 * This code was influenced by the <a
 * href="http://www.simidude.com/blog/2008/macify-a-swt-application-in-a-cross-platform-way/"
 * >CarbonUIEnhancer from Agynami</a> with the implementation being modified from the 
 * <a href="http://git.eclipse.org/c/platform/eclipse.platform.ui.git/tree/bundles/org.eclipse.e4.ui.workbench.renderers.swt.cocoa/src/org/eclipse/e4/ui/workbench/renderers/swt/cocoa/CocoaUIHandler.java"
 * >org.eclipse.e4.ui.workbench.renderers.swt.cocoa.CocoaUIHandler</a>.
 * </p>
 * @see http://www.transparentech.com/opensource/cocoauienhancer
 */
public class CocoaUIEnhancer {

	private static final int kAboutMenuItem = 0;
	private static final int kPreferencesMenuItem = 2;
	// private static final int kServicesMenuItem = 4;
	// private static final int kHideApplicationMenuItem = 6;
	private static final int kQuitMenuItem = 10;

	static long sel_toolbarButtonClicked_;
	static long sel_preferencesMenuItemSelected_;
	static long sel_aboutMenuItemSelected_;

	static Callback proc3Args;

	private IAction quitAction;
	private IAction aboutAction;
	private IAction preferencesAction;

	/**
	 * Construct a new CocoaUIEnhancer.
	 * 
	 * @param appName
	 *            The name of the application. It will be used to customize the About and Quit menu
	 *            items. If you do not wish to customize the About and Quit menu items, just pass
	 *            <tt>null</tt> here.
	 */
	public CocoaUIEnhancer( String appName ) {
	}

	/**
	 * Hook the given Listener to the Mac OS X application Quit menu and the IActions to the About
	 * and Preferences menus.
	 * 
	 * @param display
	 *            The Display to use.
	 * @param quitListener
	 *            The listener to invoke when the Quit menu is invoked.
	 * @param aboutAction
	 *            The action to run when the About menu is invoked.
	 * @param preferencesAction
	 *            The action to run when the Preferences menu is invoked.
	 */
	public void hookApplicationMenu( Display display, Listener quitListener, final IAction aboutAction, final IAction preferencesAction ) {
		// This is our callbackObject whose 'actionProc' method will be called when the About or
		// Preferences menuItem is invoked.
		//
		// Connect the given IAction objects to the actionProce method.
		//
		this.quitAction = (IAction)quitListener;
		this.aboutAction = aboutAction;
		this.preferencesAction = preferencesAction;

		Object target = new Object() {
			/** 32bit version of callback */
			@SuppressWarnings( "unused" )
			int actionProc( int id, int sel, int arg0 ) {
				return (int) actionProc((long) id, (long) sel, (long) arg0);
			}

			/** 64bit version of callback */
			long actionProc(long id, long sel, long arg0) {
				MessageDialog.openInformation(null, "Callback", "Callback received the call with: "+sel);
				if ( sel == sel_aboutMenuItemSelected_ ) {
					System.out.println("About menu item called. Invoking action...");
					if (CocoaUIEnhancer.this.aboutAction != null) CocoaUIEnhancer.this.aboutAction.run();
				} else if ( sel == sel_preferencesMenuItemSelected_ ) {
					System.out.println("Prefs menu item called. Invoking action...");
					if (CocoaUIEnhancer.this.preferencesAction != null) CocoaUIEnhancer.this.preferencesAction.run();
				} else {
					System.out.println("Unknown selection: "+sel);
					System.out.println("Known Selection for about: "+sel_aboutMenuItemSelected_);
					System.out.println("Known Selection for prefs: "+sel_preferencesMenuItemSelected_);
					// Unknown selection!
				}
				// Return value is not used.
				return 99;
			}
		};

		try {
			// Initialize the menuItems.
			initialize( target );
		} catch ( Exception e ) {
			throw new IllegalStateException( e );
		}

		// Connect the quit/exit menu.
		if ( !display.isDisposed() ) {
			if (quitListener != null) display.addListener( SWT.Close, quitListener );
		}

		// Schedule disposal of callback object
		display.disposeExec( new Runnable() {
			public void run() {
				invoke( proc3Args, "dispose" );
			}
		} );
	}

	private void initialize( Object callbackObject ) throws Exception {

		Class<?> osCls = LangUtils.forName( "org.eclipse.swt.internal.cocoa.OS" );

		// Register names in objective-c.
		if ( sel_preferencesMenuItemSelected_ == 0 ) {
			// sel_toolbarButtonClicked_ = registerName( osCls, "toolbarButtonClicked:" ); //$NON-NLS-1$
			sel_preferencesMenuItemSelected_ = registerName( osCls, "preferencesMenuItemSelected:" ); //$NON-NLS-1$
			sel_aboutMenuItemSelected_ = registerName( osCls, "aboutMenuItemSelected:" ); //$NON-NLS-1$
		}

		// Create an SWT Callback object that will invoke the actionProc method of our internal
		// callbackObject.
		proc3Args = new Callback( callbackObject, "actionProc", 3 ); //$NON-NLS-1$
		Method getAddress = Callback.class.getMethod( "getAddress", new Class[0] );
		Object object = getAddress.invoke( proc3Args, (Object[]) null );
		long proc3 = convertToLong( object );
		if ( proc3 == 0 ) {
			SWT.error( SWT.ERROR_NO_MORE_CALLBACKS );
		}

		Class<?> nsmenuCls = LangUtils.forName( "org.eclipse.swt.internal.cocoa.NSMenu" );
		Class<?> nsmenuitemCls = LangUtils.forName( "org.eclipse.swt.internal.cocoa.NSMenuItem" );
		Class<?> nsstringCls = LangUtils.forName( "org.eclipse.swt.internal.cocoa.NSString" );
		Class<?> nsapplicationCls = LangUtils.forName( "org.eclipse.swt.internal.cocoa.NSApplication" );

		// Instead of creating a new delegate class in objective-c,
		// just use the current SWTApplicationDelegate. An instance of this
		// is a field of the Cocoa Display object and is already the target
		// for the menuItems. So just get this class and add the new methods
		// to it.
		object = invoke( osCls, "objc_lookUpClass", new Object[] { "SWTApplicationDelegate" } );
		long cls = convertToLong( object );

		// Add the action callbacks for Preferences and About menu items.
		invoke( osCls, "class_addMethod", new Object[] { wrapPointer( cls ), wrapPointer( sel_preferencesMenuItemSelected_ ), wrapPointer( proc3 ), "@:@" } ); //$NON-NLS-1$
		invoke( osCls, "class_addMethod", new Object[] { wrapPointer( cls ), wrapPointer( sel_aboutMenuItemSelected_ ), wrapPointer( proc3 ), "@:@" } ); //$NON-NLS-1$

		// Get the Mac OS X Application menu.
		Object sharedApplication = invoke( nsapplicationCls, "sharedApplication" );
		Object mainMenu = invoke( sharedApplication, "mainMenu" );
		Object mainMenuItem = invoke( nsmenuCls, mainMenu, "itemAtIndex", new Object[] { wrapPointer( 0 ) } );
		Object appMenu = invoke( mainMenuItem, "submenu" );

		// Create the About <application-name> menu command
		if (aboutAction != null) {
			Object aboutMenuItem = invoke( nsmenuCls, appMenu, "itemAtIndex", new Object[] { wrapPointer( kAboutMenuItem ) } );
			invoke( nsmenuitemCls, aboutMenuItem, "setEnabled", new Object[] { true } );
			Object nsStr = invoke( nsstringCls, "stringWith", new Object[] { aboutAction.getText() } );
			invoke( nsmenuitemCls, aboutMenuItem, "setTitle", new Object[] { nsStr } );
			invoke( nsmenuitemCls, aboutMenuItem, "setAction", new Object[] { wrapPointer( sel_aboutMenuItemSelected_ ) } );
		}
		// Rename the quit action.
		if (quitAction != null) {
			Object quitMenuItem = invoke( nsmenuCls, appMenu, "itemAtIndex", new Object[] { wrapPointer( kQuitMenuItem ) } );
			Object nsStr = invoke( nsstringCls, "stringWith", new Object[] { quitAction.getText() } );
			invoke( nsmenuitemCls, quitMenuItem, "setTitle", new Object[] { nsStr } );
		}

		// Enable the Preferences menuItem.
		if (preferencesAction != null) {
			Object prefMenuItem = invoke( nsmenuCls, appMenu, "itemAtIndex", new Object[] { wrapPointer( kPreferencesMenuItem ) } );
			invoke( nsmenuitemCls, prefMenuItem, "setEnabled", new Object[] { true } );
			// Display
			Object nsStr = invoke( nsstringCls, "stringWith", new Object[] { preferencesAction.getText() } );
			invoke( nsmenuitemCls, prefMenuItem, "setTitle", new Object[] { nsStr } );
			// Action to be invoked
			invoke( nsmenuitemCls, prefMenuItem, "setAction", new Object[] { wrapPointer( sel_preferencesMenuItemSelected_ ) } );
		}

	}

	private long registerName( Class<?> osCls, String name ) throws IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Object object = invoke( osCls, "sel_registerName", new Object[] { name } );
		return convertToLong( object );
	}

	private long convertToLong( Object object ) {
		if ( object instanceof Integer ) {
			Integer i = (Integer) object;
			return i.longValue();
		}
		if ( object instanceof Long ) {
			Long l = (Long) object;
			return l.longValue();
		}
		return 0;
	}

	private static Object wrapPointer( long value ) {
		Class<?> PTR_CLASS = C.PTR_SIZEOF == 8 ? long.class : int.class;
		if ( PTR_CLASS == long.class )
			return new Long( value );
		else
			return new Integer( (int) value );
	}

	private static Object invoke( Class<?> clazz, String methodName, Object[] args ) {
		return invoke( clazz, null, methodName, args );
	}

	private static Object invoke( Class<?> clazz, Object target, String methodName, Object[] args ) {
		try {
			Class<?>[] signature = new Class<?>[args.length];
			for ( int i = 0; i < args.length; i++ ) {
				Class<?> thisClass = args[i].getClass();
				if ( thisClass == Integer.class )
					signature[i] = int.class;
				else if ( thisClass == Long.class )
					signature[i] = long.class;
				else if ( thisClass == Byte.class )
					signature[i] = byte.class;
				else if ( thisClass == Boolean.class )
					signature[i] = boolean.class;
				else
					signature[i] = thisClass;
			}
			Method method = clazz.getMethod( methodName, signature );
			return method.invoke( target, args );
		} catch ( Exception e ) {
			throw new IllegalStateException( e );
		}
	}

	private Object invoke( Class<?> cls, String methodName ) {
		return invoke( cls, methodName, (Class<?>[]) null, (Object[]) null );
	}

	private Object invoke( Class<?> cls, String methodName, Class<?>[] paramTypes, Object... arguments ) {
		try {
			Method m = cls.getDeclaredMethod( methodName, paramTypes );
			return m.invoke( null, arguments );
		} catch ( Exception e ) {
			throw new IllegalStateException( e );
		}
	}

	private Object invoke( Object obj, String methodName ) {
		return invoke( obj, methodName, (Class<?>[]) null, (Object[]) null );
	}

	private Object invoke( Object obj, String methodName, Class<?>[] paramTypes, Object... arguments ) {
		try {
			Method m = obj.getClass().getDeclaredMethod( methodName, paramTypes );
			return m.invoke( obj, arguments );
		} catch ( Exception e ) {
			throw new IllegalStateException( e );
		}
	}
}
