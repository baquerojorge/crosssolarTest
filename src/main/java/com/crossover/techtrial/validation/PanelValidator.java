/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crossover.techtrial.validation;

import com.crossover.techtrial.model.Panel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author jbaquero
 */
@Component
public class PanelValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return Panel.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Panel panel = (Panel) o;
        if(panel.getSerial() != null && panel.getSerial().length() != 16) {
            errors.rejectValue("serial", "The serial length must be 16 characters!");
        }
    }
    
}
