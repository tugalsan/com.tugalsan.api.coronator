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

    //LOADERS
    private List<TGS_Pack3<TGS_CompilerType1<T, T>, TGS_ValidatorType1<T>, /*is it stopper*/ Boolean>> pack = new ArrayList();

    public TGS_Coronator<T> anoint(TGS_CompilerType1<T, T> val) {
        pack.add(new TGS_Pack3(val, null, null));
        return this;
    }

    public TGS_Coronator<T> coronateIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        if (validate.validate(bufferedValue)) {
            pack.add(new TGS_Pack3(null, validate, true));
        }
        return this;
    }

    public TGS_Coronator<T> anointIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        if (validate.validate(bufferedValue)) {
            pack.add(new TGS_Pack3(val, validate, false));
        }
        return this;
    }

    public TGS_Coronator<T> anointAndCoronateIf(TGS_ValidatorType1<T> validate, TGS_CompilerType1<T, T> val) {
        if (validate.validate(bufferedValue)) {
            pack.add(new TGS_Pack3(val, validate, true));
        }
        return this;
    }

    //FETCHER
    public T coronate() {
        for (var comp : pack) {
            var setter = comp.value0;
            var validator = comp.value1;
            var validatorIsStopper = comp.value2;
            if (validator == null) {
                bufferedValue = setter.compile(bufferedValue);
                continue;
            }
            if (!validator.validate(bufferedValue)) {
                continue;
            }
            if (setter != null) {
                bufferedValue = setter.compile(bufferedValue);
            }
            if (validatorIsStopper) {
                return bufferedValue;
            }
        }
        return bufferedValue;
    }

}
