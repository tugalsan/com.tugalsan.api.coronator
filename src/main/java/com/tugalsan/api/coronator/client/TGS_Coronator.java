package com.tugalsan.api.coronator.client;

import com.tugalsan.api.compiler.client.*;
import com.tugalsan.api.pack.client.*;
import com.tugalsan.api.validator.client.*;
import java.util.*;

public class TGS_Coronator<T> {

    //CONSTRUCTOR
    private T bufferedValue;

    public TGS_Coronator(T initVal) {
        bufferedValue = initVal;
    }

    public static <T> TGS_Coronator<T> of(T initialValue) {
        return new TGS_Coronator(initialValue);
    }

    public static <T> TGS_Coronator<T> ofNull(T exampleValue) {
        return new TGS_Coronator(exampleValue).anoint(val -> null);
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
    private List<TGS_Pack3<TGS_CompilerType1<T, T>, TGS_ValidatorType1<T>, /*is it stopper*/ Boolean>> pack = new ArrayList();

    public TGS_Coronator<T> anoint(TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, null, null));
        return this;
    }

    public TGS_Coronator<T> coronateIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(null, validate, true));
        return this;
    }

    public TGS_Coronator<T> anointIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, validate, false));
        return this;
    }

    public TGS_Coronator<T> anointAndCoronateIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, validate, true));
        return this;
    }

    //FETCHER
    public T coronate() {
//        System.out.println("pack.size(): " + pack.size());
        var i = 0;
        for (var comp : pack) {
//            System.out.println("for i:" + i++ + ", bufferedValue: " + bufferedValue);
            var setter = comp.value0;
            var validator = comp.value1;
            var validatorIsStopper = comp.value2;
//            System.out.println("setter:" + (setter == null ? "null" : "exists") + ", validator:" + (validator == null ? "null" : "exists") + ", validatorIsStopper:" + (validatorIsStopper == null ? "null" : "exists"));
            if (validator == null) {
//                System.out.println("validator == null, set");
                bufferedValue = setter.compile(bufferedValue);
                continue;
            }
            if (bufferedValue == null || !validator.validate(bufferedValue)) {
//                System.out.println("!validator.validate(bufferedValue)");
                continue;
            }
            if (setter != null) {
//                System.out.println("setter != null");
                bufferedValue = setter.compile(bufferedValue);
            }
            if (validatorIsStopper) {
//                System.out.println("validatorIsStopper == true");
                return bufferedValue;
            }
//            System.out.println("fin");
        }
        return bufferedValue;
    }

}
