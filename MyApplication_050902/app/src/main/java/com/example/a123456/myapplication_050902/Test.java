package com.example.a123456.myapplication_050902;

import static org.junit.Assert.assertEquals;

public class Test {


    @org.junit.Test
    public void testlogin(){



        assertEquals(false, new LoginActivity().login("1", "11"));

    }



}
