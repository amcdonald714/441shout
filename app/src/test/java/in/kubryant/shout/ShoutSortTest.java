package in.kubryant.shout;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

/**
 * Created by allison on 4/19/15.
 */
public class ShoutSortTest {

    @Test
    public void testCompare() {
        ArrayList<Shout> shoutList = new ArrayList<Shout>();

        Shout s1 = new Shout();
        s1.setMsg("hello1");
        s1.setTime("1");
        s1.setTimeRecv("12");
        Shout s2 = new Shout();
        s2.setMsg("hello2");
        s2.setTime("31");
        s2.setTimeRecv("5");
        Shout s3 = new Shout();
        s3.setMsg("hello3");
        s3.setTime("11");
        s3.setTimeRecv("26");
        Shout s4 = new Shout();
        s4.setMsg("hello4");
        s4.setTime("21");
        s4.setTimeRecv("10");

        shoutList.add(s1);
        shoutList.add(s2);
        shoutList.add(s3);
        shoutList.add(s4);

        Collections.sort(shoutList, Shout.CreatedTimeSort);
        System.out.println(shoutList.toString());
        Assert.assertEquals(shoutList.get(0).getMsg(), "hello2");
        Assert.assertEquals(shoutList.get(1).getMsg(), "hello4");
        Assert.assertEquals(shoutList.get(2).getMsg(), "hello3");
        Assert.assertEquals(shoutList.get(3).getMsg(), "hello1");

        Collections.sort(shoutList, Shout.ReceivedTimeSort);
        System.out.println(shoutList.toString());
        Assert.assertEquals(shoutList.get(0).getMsg(), "hello3");
        Assert.assertEquals(shoutList.get(1).getMsg(), "hello1");
        Assert.assertEquals(shoutList.get(2).getMsg(), "hello4");
        Assert.assertEquals(shoutList.get(3).getMsg(), "hello2");
    }

}
