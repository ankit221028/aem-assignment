package com.aem.assignment.core.schedulers;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Component(service = Runnable.class)
@Designate(ocd=ProductScheduler.Config.class)
public class ProductScheduler implements Runnable{
    @ObjectClassDefinition(name = "Product node creation scheduler",description = "Hand made scheduler")
    public static @interface Config{
        @AttributeDefinition(name = "Cron-job expression")
                //Get the Cron expression by going on to Cron-maker and mention the Time
        String scheduler_expression() default "";

        @AttributeDefinition(name = "concurrent task")
        boolean scheduler_concurrent() default false;

    }

    @Override
    public void run() {

    }
}
