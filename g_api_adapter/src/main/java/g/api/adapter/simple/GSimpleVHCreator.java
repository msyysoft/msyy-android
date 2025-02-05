package g.api.adapter.simple;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class GSimpleVHCreator<T> implements GSimpleVHCreatorSuper<T> {

    private final Constructor<?> mConstructor;
    private Object[] mInstanceObjects;

    private GSimpleVHCreator(Constructor<?> constructor, Object[] instanceObjects) {
        mConstructor = constructor;
        mInstanceObjects = instanceObjects;
    }

    public static <MyAdapterData> GSimpleVHCreatorSuper<MyAdapterData> create(final Object enclosingInstance, final Class<?> cls, final Object... args) {
        if (cls == null) {
            throw new IllegalArgumentException("ViewHolderClass is null.");
        }

        // top class
        boolean isEnclosingInstanceClass = false;
        if (cls.getEnclosingClass() != null && !Modifier.isStatic(cls.getModifiers())) {
            isEnclosingInstanceClass = true;
        }

        // inner instance class should pass enclosing class, so +1
        int argsLen = isEnclosingInstanceClass ? args.length + 1 : args.length;

        final Object[] instanceObjects = new Object[argsLen];

        int copyStart = 0;
        // if it is inner instance class, first argument should be the enclosing class instance
        if (isEnclosingInstanceClass) {
            instanceObjects[0] = enclosingInstance;
            copyStart = 1;
        }

        // has copy construction parameters
        if (args.length > 0) {
            System.arraycopy(args, 0, instanceObjects, copyStart, args.length);
        }

        // fill the types
        final Class[] parameterTypes = new Class[argsLen];
        for (int i = 0; i < instanceObjects.length; i++) {
            parameterTypes[i] = instanceObjects[i].getClass();
        }

        Constructor<?> constructor = null;
        try {
            constructor = cls.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (constructor == null) {
            throw new IllegalArgumentException("ViewHolderClass can not be initiated");
        }

        GSimpleVHCreatorSuper lazyCreator = new GSimpleVHCreator(constructor, instanceObjects);
        return lazyCreator;
    }

    @Override
    public GSimpleViewHolder<T> createViewHolder(int position) {
        Object object = null;
        try {
            boolean isAccessible = mConstructor.isAccessible();
            if (!isAccessible) {
                mConstructor.setAccessible(true);
            }
            object = mConstructor.newInstance(mInstanceObjects);
            if (!isAccessible) {
                mConstructor.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (object == null || !(object instanceof GSimpleViewHolder)) {
            throw new IllegalArgumentException("ViewHolderClass can not be initiated");
        }
        return (GSimpleViewHolder<T>) object;
    }
}
