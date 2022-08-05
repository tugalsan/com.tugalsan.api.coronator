package com.tugalsan.api.coronator.client;

import com.tugalsan.api.compiler.client.*;
import com.tugalsan.api.pack.client.*;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.validator.client.*;
import java.util.*;

public class TGS_Coronator<T> {

    //CONSTRUCTOR
    private T bufferedValue;

    public TGS_Coronator(T initVal) {
        bufferedValue = initVal;
    }

    public static <T> TGS_Coronator<T> of(Class<T> clazz) {
        return new TGS_Coronator(null);
    }

    public static <T> TGS_Coronator<T> of(T initialValue) {
        return new TGS_Coronator(initialValue);
    }

    public static TGS_Coronator<String> ofStr() {
        return new TGS_Coronator(null);
    }

    public static TGS_Coronator<Long> ofLng() {
        return new TGS_Coronator(null);
    }

    public static TGS_Coronator<Integer> ofInt() {
        return new TGS_Coronator(null);
    }

    public static TGS_Coronator<Double> ofDbl() {
        return new TGS_Coronator(null);
    }

    //LOADERS
    private final List<TGS_Pack3<TGS_CompilerType1<T, T>, TGS_ValidatorType1<T>, Type>> pack = new ArrayList();

    private enum Type {
        SKIPPER, STOPPER
    }

    public TGS_Coronator<T> anoint(TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, null, Type.SKIPPER));
        return this;
    }

    public TGS_Coronator<T> coronateIf(TGS_ValidatorType1<T> validate) {
        pack.add(new TGS_Pack3(null, validate, Type.STOPPER));
        return this;
    }

    public TGS_Coronator<T> anointIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, validate, Type.SKIPPER));
        return this;
    }

    public TGS_Coronator<T> anointAndCoronateIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, validate, Type.STOPPER));
        return this;
    }

    //EXCEPTION HANDLING
    public TGS_Pack2<T, Exception> coronateWithException() {
        return TGS_UnSafe.compile(() -> TGS_Pack2.of(coronate(), null), e -> TGS_Pack2.of(null, e));
    }

    public T coronateAs(TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, null, Type.STOPPER));
        return coronate();
    }

//FETCHER
    public T coronate() {
//        System.out.println("pack.size(): " + pack.size());
//        var i = 0;
        TGS_CompilerType1<T, T> exceptionHandler = null;
        for (var comp : pack) {
//            System.out.println("for i:" + i++ + ", bufferedValue: " + bufferedValue);
            var setter = comp.value0;
            var validator = comp.value1;
            var type = comp.value2;
//            System.out.println("setter:" + (setter == null ? "null" : "exists") + ", validator:" + (validator == null ? "null" : "exists") + ", validatorIsStopper:" + (validatorIsStopper == null ? "null" : "exists"));
            if (validator == null) {
//                System.out.println("validator == null, set");
                bufferedValue = setter.compile(bufferedValue);
                continue;
            }
            if (!validator.validate(bufferedValue)) {
//                System.out.println("!validator.validate(bufferedValue)");
                continue;
            }
            if (setter != null) {
//                System.out.println("setter != null");
                bufferedValue = setter.compile(bufferedValue);
            }
            if (type == Type.STOPPER) {
//                System.out.println("validatorIsStopper == true");
                return bufferedValue;
            }
//            System.out.println("fin");
        }
        return bufferedValue;
    }
}
