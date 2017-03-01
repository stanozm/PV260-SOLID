package pv260.solid.lsp.original;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serialize and deserialize any object which satisfies these conditions:<ul>
 * <li>All fields are either a primitive type, String, or array of the previous
 * (array of arrays are not permissible)</li>
 * <li>The serialized object directly extends Object
 * (the object can however implement any interfaces)</li>
 * <li>The serialized object does not declare a generic parameter</li>
 * </ul>
 * If any of these conditions is not satisfied, the behavior is not defined.
 * All implementation must have the property that serialize and deserialize are inverses of each other
 * that is it must always hold that:<ul>
 * <li>X.equals(serialize(deserialize(X))) == true</li>
 * <li>Y.equals(deserialize(serialize(Y))) == true</li>
 * </ul>
 * Note that when instance is created, the first (order is JVM dependent and thus not specified here)
 * constructor available is used, all constructors of classes serialized using this interface
 * should thus be side effect free
 * <p>
 * Static fields should be ignored and not added to de serialized form
 * <p>
 * All implementations of this interface must be Thread Safe, preferably by being stateless
 */
public interface SimpleSerializer {

    /**
     *
     * @param instance satisfying the conditions imposed in contract of this class
     * @param into serialized representation will be written here,
     *              it is not closed here
     * @throws IOException if any exception occurs while working with the stream
     */
    public void serializeInto(Object instance, OutputStream into) throws IOException;

    /**
     *
     * @param from source containing the object to be deserialized,
     *              it is not closed here,
     *              handling of any extra data, that is any data not part of the deserialized object,
     *              in the input is implementation specific
     * @return deserialized object if the input contains serialized object
     * conforming to contract of this class, otherwise behavior is undefined
     */
    public Object deserializeFrom(InputStream from) throws IOException;

}
