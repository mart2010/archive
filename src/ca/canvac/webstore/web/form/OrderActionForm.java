package ca.canvac.webstore.web.form;

import ca.canvac.webstore.domain.Order;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import javax.servlet.http.HttpServletRequest;

public class OrderActionForm extends ValidatorForm {

    private Order order;

    /* Constructors */

    public OrderActionForm() {
        this.order = new Order();
    }

    /* JavaBeans Properties */

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

}