package org.javaboy.flowableprocess;

import org.apache.commons.io.FileUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 江南一点雨
 * @微信公众号 江南一点雨
 * @网站 http://www.itboyhub.com
 * @国际站 http://www.javaboy.org
 * @微信 a_java_boy
 * @GitHub https://github.com/lenve
 * @Gitee https://gitee.com/lenve
 */

/**
 * 绘制流程图的测试
 */
@SpringBootTest
public class ProcessImageTest {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Test
    void test03() throws IOException {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExclusiveGatewayDemo01").latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("ExclusiveGatewayDemo01").singleResult();
        if (hpi == null) {
            //说明目前没有正在执行的流程，那么直接返回即可
            return;
        }
        //绘制图片的一个生成器
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        //所有已经执行的活动的 ID，将之保存在这个集合中
        List<String> highLightedActivities = new ArrayList<>();
        //所有已经执行过的线条，将之保存在这个集合中
        List<String> highLightedFlows  = new ArrayList<>();
        double scaleFactor = 1.0;
        boolean drawSequenceFlowNameWithNoLabelDI=true;
        //查询当前流程实例的所有活动信息
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(hpi.getId()).list();
        for (HistoricActivityInstance ai : list) {
            //如果活动的类型是 sequenceFlow，那么就加入到线条的 ID 中
            if (ai.getActivityType().equals("sequenceFlow")) {
                highLightedFlows.add(ai.getActivityId());
            }else{
                //否则加入到活动的高亮的 ID 中
                highLightedActivities.add(ai.getActivityId());
            }
        }
        InputStream is = generator.generateDiagram(bpmnModel, "PNG", highLightedActivities, highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
        FileUtils.copyInputStreamToFile(is,new File("/Users/sang/workspace/tienchin-video/3.png"));
        is.close();
    }


    @Test
    void test02() throws IOException {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExclusiveGatewayDemo01").latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        //查询当前的流程实例，因为这里只有一个流程实例，所以直接用 singleResult 查询即可
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().singleResult();
        if (pi == null) {
            //说明目前没有正在执行的流程，那么直接返回即可
            return;
        }
        //绘制图片的一个生成器
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        //所有已经执行的活动的 ID，将之保存在这个集合中
        List<String> highLightedActivities = new ArrayList<>();
        //所有已经执行过的线条，将之保存在这个集合中
        List<String> highLightedFlows  = new ArrayList<>();
        double scaleFactor = 1.0;
        boolean drawSequenceFlowNameWithNoLabelDI=true;
        //查询当前流程实例的所有活动信息
        List<ActivityInstance> list = runtimeService.createActivityInstanceQuery().processInstanceId(pi.getId()).list();
        for (ActivityInstance ai : list) {
            //如果活动的类型是 sequenceFlow，那么就加入到线条的 ID 中
            if (ai.getActivityType().equals("sequenceFlow")) {
                highLightedFlows.add(ai.getActivityId());
            }else{
                //否则加入到活动的高亮的 ID 中
                highLightedActivities.add(ai.getActivityId());
            }
        }
        InputStream is = generator.generateDiagram(bpmnModel, "PNG", highLightedActivities, highLightedFlows, scaleFactor, drawSequenceFlowNameWithNoLabelDI);
        FileUtils.copyInputStreamToFile(is,new File("/Users/sang/workspace/tienchin-video/2.png"));
        is.close();
    }

    @Test
    void test01() throws IOException {
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey("ExclusiveGatewayDemo01").latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());
        //绘制图片的一个生成器
        DefaultProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();
        //1. 绘制流程图的对象
        //2. 缩放因子
        //3. 是否在绘制连接线的时候，是否绘制对应的文本
        InputStream is = generator.generatePngDiagram(bpmnModel, 1.0, true);
        //将 IO 流解析为一张图片
        FileUtils.copyInputStreamToFile(is,new File("/Users/sang/workspace/tienchin-video/1.png"));
        is.close();
    }
}
