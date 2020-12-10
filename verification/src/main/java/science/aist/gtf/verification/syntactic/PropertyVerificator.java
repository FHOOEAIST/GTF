/*
 * Copyright (c) 2020 the original author or authors.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package science.aist.gtf.verification.syntactic;

import lombok.Getter;
import lombok.Setter;
import science.aist.gtf.verification.syntactic.visitor.factory.RestrictedVisitorFactory;
import science.aist.gtf.verification.syntactic.visitor.factory.impl.SingletonVisitorFactory;


/**
 * <p>Property verificator which checks if all containers of a class are
 * initialized and filled and all fields are not only initialized but also set</p>
 * <p>
 * You can use the PropertyVerificator with a spring configuration file like:
 * <pre>
 *   {@code <bean id="propertyVerificator" class="science.aist.gtf.verification.syntactic.PropertyVerificator"/>}
 * </pre>
 * <p>
 * or with own defined {@link PropertyRestrictor} like:
 *
 * <pre>
 *   {@code
 *      <bean id="propertyVerificator" class="science.aist.gtf.verification.syntactic.PropertyVerificator">
 *        <property name="restrictor">
 *            <bean class="science.aist.gtf.verification.syntactic.PropertyRestrictor">
 *                <!-- see {@link PropertyRestrictor} class description  -->
 *             </bean>
 *         </property>
 *     </bean>
 *     }
 * </pre>
 *
 * @author Andreas Pointner
 * @author Christoph Praschl
 * @since 1.0
 */
@Getter
@Setter
public class PropertyVerificator<Input> {

    private RestrictedVisitorFactory visitorFactory = new SingletonVisitorFactory(new PropertyRestrictor());

    /**
     * @return the {@link PropertyRestrictor} used in {@link PropertyVerificator#visitorFactory}
     */
    public PropertyRestrictor getRestrictor() {
        return visitorFactory.getRestrictor();
    }

    /**
     * @param restrictor sets the {@link PropertyRestrictor} used in {@link PropertyVerificator#visitorFactory}
     */
    public void setRestrictor(PropertyRestrictor restrictor) {
        visitorFactory.setRestrictor(restrictor);
    }


    /**
     * Function which searches recursively in obj and nested objects in obj for null fields, empty collections only
     * default initialized primitive fields
     *
     * @param obj obj which should be checked
     * @return a Map with obj and its nested objects connected to all invalid fields and the reason of invalidity
     */

    public PropertyVerificatorResult getInvalidProperties(Input obj) {
        return visitorFactory.createObjectVisitor().visit(obj);
    }
}
