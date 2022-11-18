package org.javaboy.flowableprocess;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.javaboy.flowableprocess.model.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

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
 * 这个是我自定义的流程部署接口
 */
@RestController
public class ProcessDeployController {
    //RepositoryService 这个实体类可以用来操作 ACT_RE_XXX 这种表
    @Autowired
    RepositoryService repositoryService;


    /**
     * 部署带表单的流程
     *
     * 这种 HTML 表单，部署的时候需要跟流程一起部署，需要跟流程具备相同的部署 ID，否则，将来在流程运行的时候，就查询不到这个流程中所使用的外置表单
     *
     * 具体使用，就是将来部署流程的时候，同时上传该该流程的流程部署文件和该流程中所使用的外置表单
     *
     * @param files
     * @return
     */
    @PostMapping("/deploywithform")
    public RespBean deloyProcessWithForm(MultipartFile[] files) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService
                //开始流程部署的构建
                .createDeployment()
                .name("JAVABOY的工作流")
                .category("我的流程分类")
                .key("我的自定义的工作流的 KEY");
        for (MultipartFile file : files) {
            deploymentBuilder.addInputStream(file.getOriginalFilename(), file.getInputStream());
        }
        //完成项目的部署
        Deployment deployment = deploymentBuilder.deploy();
        return RespBean.ok("部署成功", deployment.getId());
    }

    /**
     * 这个就是我的流程部署接口，流程部署将来要上传一个文件，这个文件就是流程的 XML 文件
     *
     * @param file
     * @param tenantId 这个是租户 id，用来区分这个流程是属于哪一个租户的
     * @return
     */
    @PostMapping("/deploy")
    public RespBean deploy(MultipartFile file,String tenantId) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService
                //开始流程部署的构建
                .createDeployment()
                .name("JAVABOY的工作流")//ACT_RE_DEPLOYMENT 表中的 NAME_ 属性
                .category("我的流程分类")//ACT_RE_DEPLOYMENT 表中的 CATEGORY_ 属性
                .key("我的自定义的工作流的 KEY")//ACT_RE_DEPLOYMENT 表中的 KEY_ 属性
//                .tenantId(tenantId)
                //也可以用这个方法代替 addInputStream，但是注意，这个需要我们自己先去解析 IO 流程，将 XML 文件解析为一个字符串，然后就可以调用这个方法进行部署了
//                .addString()
                //设置文件的输入流程，将来通过这个输入流自动去读取 XML 文件
                .addInputStream(file.getOriginalFilename(), file.getInputStream());
        //完成项目的部署
        Deployment deployment = deploymentBuilder.deploy();
        return RespBean.ok("部署成功", deployment.getId());
    }

    @PostMapping("/deploy2")
    public RespBean deploy2(MultipartFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len;
        byte[] buf = new byte[1024];
        InputStream is = file.getInputStream();
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        is.close();
        DeploymentBuilder deploymentBuilder = repositoryService
                //开始流程部署的构建
                .createDeployment()
                .name("JAVABOY的工作流")//ACT_RE_DEPLOYMENT 表中的 NAME_ 属性
                .category("我的流程分类")//ACT_RE_DEPLOYMENT 表中的 CATEGORY_ 属性
                .key("我的自定义的工作流的 KEY")//ACT_RE_DEPLOYMENT 表中的 KEY_ 属性
                //也可以用这个方法代替 addInputStream，但是注意，这个需要我们自己先去解析 IO 流程，将 XML 文件解析为一个字符串，然后就可以调用这个方法进行部署了
//                .addString()
                //设置文件的输入流程，将来通过这个输入流自动去读取 XML 文件
//                .addInputStream(file.getOriginalFilename(), file.getInputStream());
                //注意这里需要设置资源名称，这个资源名称不能随意取值，建议最好和文件名保持一致
                .addBytes(file.getOriginalFilename(), baos.toByteArray());
        //完成项目的部署
        Deployment deployment = deploymentBuilder.deploy();
        return RespBean.ok("部署成功", deployment.getId());
    }


    @PostMapping("/deploy3")
    public RespBean deploy3(MultipartFile file) throws IOException {
        DeploymentBuilder deploymentBuilder = repositoryService
                //开始流程部署的构建
                .createDeployment()
                .name("JAVABOY的工作流")//ACT_RE_DEPLOYMENT 表中的 NAME_ 属性
                .category("我的流程分类")//ACT_RE_DEPLOYMENT 表中的 CATEGORY_ 属性
                .key("我的自定义的工作流的 KEY")//ACT_RE_DEPLOYMENT 表中的 KEY_ 属性
                //设置流程定义激活的时间，在到达这个时间点之前，这个流程定义是不可用的，到达这个时间点之后，就可以通过这个流程定义启动一个流程实例了
                .activateProcessDefinitionsOn(new Date(System.currentTimeMillis() + 10 * 1000))
                .addInputStream(file.getOriginalFilename(), file.getInputStream());
        //完成项目的部署
        Deployment deployment = deploymentBuilder.deploy();
        return RespBean.ok("部署成功", deployment.getId());
    }

}
