package be.infowhere.moviebatles.utils.impl;

import be.infowhere.moviebatles.utils.NumberUtils;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class NumberUtilsImpl implements NumberUtils {
    @Override
    public int getRandomNumber(int size) {
        return new Random()
                .ints(0, (size - 1))
                .limit(1)
                .findFirst()
                .getAsInt();
    }

    @Override
    public int getQtdCombinations(List<String> values) {
        if(values==null || values.size() <= 1){
            return 0;
        }

        return Sets.cartesianProduct(
                Sets.newHashSet(values),
                Sets.newHashSet(values))
                .size() - (values.size()*2);

    }

}
