package ke.co.toshngure.dataloading;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Anthony Ngure on 23/01/2018.
 * Email : anthonyngure25@gmail.com.
 */

class Utils {

     static Gson getSafeGson(final String... avoidNames) {
        return new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                for (String avoidName : avoidNames) {
                    if (fieldAttributes.getName().equals(avoidName)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }
}
