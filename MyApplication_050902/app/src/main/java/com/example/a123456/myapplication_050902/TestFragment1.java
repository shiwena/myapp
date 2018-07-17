
package com.example.a123456.myapplication_050902;

import static org.junit.Assert.assertEquals;

public class TestFragment1 {

    @org.junit.Test
    public void testlogin(){
        // test login
        assertEquals(true, new MyFragment1().InsertDate(80, 50,80,37,3.0));
    }

    public void testlogin1(){
        // test login
        assertEquals(false, new MyFragment1().InsertDate(0, 50,80,37,3.0));
    }

    public void testlogin2(){
        // test login
        assertEquals(false, new MyFragment1().InsertDate(80, 0,80,37,3.0));
    }

    public void testlogin3(){
        // test login
        assertEquals(false, new MyFragment1().InsertDate(80, 50,0,37,3.0));
    }

    public void testlogin4(){
        // test login
        assertEquals(false, new MyFragment1().InsertDate(80, 50,80,0,3.0));
    }

    public void testlogin5(){
        // test login
        assertEquals(false, new MyFragment1().InsertDate(80, 50,80,37,0));
    }

}
