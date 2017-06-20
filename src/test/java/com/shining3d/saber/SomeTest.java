package com.shining3d.saber;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Created by chengpanwang on 2016/11/18.
 */
public class SomeTest {

    @Test
    public void testReplace() {
        String a = "<img alt=\"美研发机器蜘蛛太空中3D打印卫星：或制造出更大型的太空船\" src=\"http://www.3ddayin.net/uploads/allimg/161103/0U5024143-0.jpg?imageView&amp;thumbnail=550x0\" /> ";
        a = StringUtils.replace(a, "http://www.3ddayin.net/uploads/allimg/161103/0U5024143-0.jpg?imageView&amp;thumbnail=550x0", "abcg");
        System.out.println(a);
    }

}
