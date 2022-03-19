package be.infowhere.moviebatles.util;

import be.infowhere.moviebatles.utils.NumberUtils;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NumberUtilsTest {

    @Autowired
    private NumberUtils numberUtils;

    @Test
    public void checkCombinationNull() throws Exception{
        assertEquals(0,numberUtils.getQtdCombinations(null));
    }

    @Test
    public void checkCombinationOnlyOneData() throws Exception{
        assertEquals(0,numberUtils.getQtdCombinations(
                List.of("teste"))
        );
    }

    @Test
    public void checkCombinationWithDatas() throws Exception{
        assertEquals(3,numberUtils.getQtdCombinations(
                List.of("teste","teste2","test3"))
        );
    }

}
