package template;

import io.ideploy.deployment.common.ModuleUserShellArgs;
import io.ideploy.deployment.common.enums.ModuleRepoType;
import io.ideploy.deployment.common.enums.ModuleType;
import io.ideploy.deployment.common.util.EncryptUtil;
import io.ideploy.deployment.compile.vo.CompileRequest;
import io.ideploy.deployment.compile.vo.CompileShellTemplate;
import io.ideploy.deployment.constant.tpl.CompileTplArgs;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * 详情 :
 * <p>
 * 详细 :
 *
 * @author K-Priest 17/3/28
 */
public class CompileShellTemplateTest {

    @Test
    public void testGenerateShell() throws Exception {
        CompileRequest compileRequest = buildRequest();
        compileRequest.setVersion("" + System.currentTimeMillis());

        CompileShellTemplate shellTemplate = new CompileShellTemplate("platform-admin", compileRequest, "monitor_shell_log_upload.py");
        String compileShellFile = shellTemplate.generate();
        assert StringUtils.isNotBlank(compileShellFile);
    }

    private CompileRequest buildRequest() {
        CompileRequest compileRequest = new CompileRequest();
        compileRequest.setModuleName("platform-admin");
        compileRequest.setProjectName("platform");
        compileRequest.setEnv("dev");
        compileRequest.setHistoryId(20);
        compileRequest.setSvnUserName("kevin");
        compileRequest.setSvnPassword("123123");
        compileRequest.setRepoType(ModuleRepoType.GIT.getValue());
        compileRequest.setTagName("/master");
        compileRequest.setSvnAddr("http://gits-lan.ibeiliao.net/platform/platform-parent.git");
        compileRequest.setCompileShell("mvn -P=${env} -Dmaven.test.skip=true -U clean install \n cp -R ${"+ ModuleUserShellArgs.compileDir+"}*.war ${targetDir} ");
        return compileRequest;
    }


    @Test
    public void test() {
        CompileRequest compileRequest = buildRequest();
        String checkoutShell = "yes 2>/dev/null |";
        String checkoutDir = "/data/project/dev/platform-parent//master";
        String tagName = "branches/20170401_git";

        String repoURL = compileRequest.getSvnAddr();
        if (compileRequest.getRepoType() == ModuleRepoType.SVN.getValue()) {
            checkoutShell = checkoutShell + "svn co " + repoURL + "/" + tagName + " " + checkoutDir
                    + " --no-auth-cache --username='" + compileRequest.getSvnUserName() + "' --password='" + compileRequest.getSvnPassword() + "'";
        } else {
            System.out.println();
            if (tagName.startsWith("/")) {
                tagName = tagName.substring(1, tagName.length());
            }
            if (tagName.endsWith("/")) {
                tagName = tagName.substring(0, tagName.length() - 1);
            }
            checkoutShell = "git clone ";
            String[] gitURLStartArr = {"http://", "https://", "git"};
            for (String startStr : gitURLStartArr) {
                if (repoURL.startsWith(startStr)) {
                    String replaceStr = startStr + compileRequest.getSvnUserName() + ":"
                            + compileRequest.getSvnPassword() + "@";
                    checkoutShell = checkoutShell + repoURL.replaceFirst(startStr, replaceStr) + " " + checkoutDir + "\n";
                    break;
                }
            }
            checkoutShell = checkoutShell + "cd " + checkoutDir + "\n" + "git fetch --all\n";

            if (tagName.startsWith("branches/")) {
                checkoutShell = checkoutShell + "git checkout  " + tagName.substring("branches/".length(), tagName.length());
            }

            if (tagName.startsWith("tags/")) {
                checkoutShell = checkoutShell + "git checkout " + tagName;
            }
        }
        System.out.println(checkoutShell);
    }

    @Test
    public void testStaticProject() throws IOException {
        CompileRequest request = new CompileRequest();
        request.setModuleName("support-kms-front");
        request.setModuleId(29);
        request.setCompileShell("cp -rf ${compileDir} ${targetDir}");
        request.setEnv("dev");
        request.setRepoType(ModuleRepoType.SVN.getValue());
        request.setSvnAddr("http://svn.ibeiliao.net/svn/beiliao/beiliao/support-kms-parent");
        request.setSvnUserName("K-Priest");
        request.setSvnPassword("bBl03413pPXD");
        request.setModuleType(ModuleType.STATIC.getValue());
        request.setTagName("trunk");
        request.setVersion("26841");
        request.setProjectId(18);
        request.setProjectName("support-kms-parent");
        CompileShellTemplate template = new CompileShellTemplate("support-kms-front", request, "monitor_shell_log_upload.py");
        String ossName = request.getModuleId() + "_" + EncryptUtil.getMD5(request.getModuleId() + "|" + request.getEnv() + "|" + request.getTagName() + "|" + request.getVersion());
        String compileShellFile = template.generate();
        System.out.println(compileShellFile);
    }
}
