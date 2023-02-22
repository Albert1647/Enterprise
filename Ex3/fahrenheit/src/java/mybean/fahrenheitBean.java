/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mybean;

import javax.ejb.Stateless;

/**
 *
 * @author Takodachi
 */
@Stateless
public class fahrenheitBean implements fahrenheitBeanRemote {

    @Override
    public float getFahrenheit(int c) {
        return (5.0f/9.0f) * (c-32);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
