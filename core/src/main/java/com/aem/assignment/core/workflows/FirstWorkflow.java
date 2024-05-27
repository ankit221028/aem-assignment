package com.aem.assignment.core.workflows;

import com.day.cq.workflow.WorkflowException;
import com.day.cq.workflow.WorkflowSession;
import com.day.cq.workflow.exec.WorkItem;
import com.day.cq.workflow.exec.Workflow;
import com.day.cq.workflow.exec.WorkflowProcess;
import com.day.cq.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

@Component(service = WorkflowProcess.class,immediate = true,property = {
        "process.label"+" = Practice workflow process"
})
public class FirstWorkflow implements WorkflowProcess {
    private static final Logger log = (Logger) LoggerFactory.getLogger(FirstWorkflow.class);
    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {

    }
}
