package io.github.rezeros.test.v2;

import io.github.rezeros.beans.propertyeditors.CustomBooleanEditor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Author: ReZero
 * @Date: 3/31/19 10:00 PM
 * @Version 1.0
 */
public class CustomBooleanEditorTest {

    @Test
    public void testConvertStringToBoolean(){
        CustomBooleanEditor editor = new CustomBooleanEditor(true);

        editor.setAsText("true");
        Assert.assertEquals(true, ((Boolean)editor.getValue()).booleanValue());
        editor.setAsText("false");
        Assert.assertEquals(false, ((Boolean)editor.getValue()).booleanValue());

        editor.setAsText("on");
        Assert.assertEquals(true, ((Boolean)editor.getValue()).booleanValue());
        editor.setAsText("off");
        Assert.assertEquals(false, ((Boolean)editor.getValue()).booleanValue());


        editor.setAsText("yes");
        Assert.assertEquals(true, ((Boolean)editor.getValue()).booleanValue());
        editor.setAsText("no");
        Assert.assertEquals(false, ((Boolean)editor.getValue()).booleanValue());


        try{
            editor.setAsText("aabbcc");
        }catch(IllegalArgumentException e){
            return;
        }
        Assert.fail();


    }


}
