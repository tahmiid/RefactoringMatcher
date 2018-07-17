package ca.concordia.refactoringmatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.refactoringminer.util.GitServiceImpl;

import sun.misc.Lock;

public class ExtendedGitServiceImpl extends GitServiceImpl implements ExtendedGitService {

	@Override
	public synchronized void checkout(Repository repository, String commitId) throws Exception {
//	    logger.info("Checking out {} {} ...", repository.getDirectory().getParent().toString(), commitId);
		Lock lock = new Lock();
		lock.lock();
		try {
		    // access to the shared resource
		} finally {
		    lock.unlock();
		}
	    super.checkout(repository, commitId);
//		File workingDir = repository.getDirectory().getParentFile();
//		ExternalProcess.execute(workingDir, "git", "checkout", commitId);
	}
	
	public String getNextTag(Repository repository, String commitId) throws Exception {
		String nextRelease = "";
		final Set<Ref> tags = new HashSet<Ref>();
		final RevWalk walk = new RevWalk(repository);
		walk.reset();
		ObjectId id = repository.resolve(commitId);
		RevCommit commit = walk.parseCommit(id);
		walk.reset();
		for (final Ref ref : repository.getTags().values()) {
			final RevObject obj = walk.parseAny(ref.getObjectId());
			final RevCommit tagCommit;
			if (obj instanceof RevCommit) {
				tagCommit = (RevCommit) obj;
			} else if (obj instanceof RevTag) {
				tagCommit = walk.parseCommit(((RevTag) obj).getObject());
			} else {
				continue;
			}
			if (commit.equals(tagCommit) || walk.isMergedInto(commit, tagCommit)) {
				tags.add(ref);
				nextRelease = ref.getName();
				break;
			}
		}
		return nextRelease;
	}

	public Repository duplicate(Repository repository) throws Exception {
		String source = repository.getDirectory().getAbsolutePath().replaceAll("\\.git", "") ;
		File srcDir = new File(source);

		int rand = ThreadLocalRandom.current().nextInt(1,1000);
		String destination = source.substring(0, source.length()-2) + rand;
		File destDir = new File(destination);

		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		    return super.openRepository(destination);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return null;
	}
	
	public Set<Ref> getAllReleaseTags(Repository repository) throws Exception {
		final Set<Ref> tags = new HashSet<Ref>();
		final RevWalk walk = new RevWalk(repository);
		walk.reset();
		for (final Ref ref : repository.getTags().values()) {
			final RevObject obj = walk.parseAny(ref.getObjectId());
			if (!(obj instanceof RevCommit) && !(obj instanceof RevTag)) {
				continue;
			}
			tags.add(ref);
		}
		return tags;
	}

	@Override
	public ArrayList<String> getAllCommits(Repository repository) throws Exception {
		ArrayList<String> commits = new ArrayList<String>();
		String treeName = "refs/heads/master"; // tag or branch
		try(Git git = new Git(repository)) {
			for (RevCommit commit : git.log().add(repository.resolve(treeName)).call()) {
			    commits.add(commit.getName());
			}
		} catch (RevisionSyntaxException | GitAPIException | IOException e) {
			e.printStackTrace();
			throw e;
		}
		return commits;
	}
}
