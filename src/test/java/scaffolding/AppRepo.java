package scaffolding;

import com.danielflower.restabuild.build.RepoBranch;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.transport.URIish;

import java.io.File;
import java.net.URISyntaxException;

import static scaffolding.Photocopier.copySampleAppToTempDir;

public class AppRepo {

    public static AppRepo create(String name) {
        try {
            File originDir = copySampleAppToTempDir(name);

            InitCommand initCommand = Git.init();
            initCommand.setDirectory(originDir);
            Git origin = initCommand.call();

            origin.add().addFilepattern(".").call();
            origin.commit().setMessage("Initial commit").call();
            origin.branchCreate().setName("branch-1").call();

            origin.branchCreate().setName("branch-2").call();
            origin.branchCreate().setName("branch-3").call();
            origin.branchCreate().setName("branch-4").call();


            return new AppRepo(name, originDir, origin);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating git repo", e);
        }
    }

    public final String name;
    public final File originDir;
    public final Git origin;

    private AppRepo(String name, File originDir, Git origin) {
        this.name = name;
        this.originDir = originDir;
        this.origin = origin;
    }

    public RepoBranch toRepoBranch(String branch) {
        try {
            return new RepoBranch(new URIish(gitUrl()), branch);
        } catch (URISyntaxException e) {
            throw new RuntimeException("URIish!!", e);
        }
    }

    public String gitUrl() {
        return originDir.toURI().toString();
    }
}
