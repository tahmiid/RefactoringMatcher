package ca.concordia.refactoringmatcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;

import ca.concordia.refactoringmatcher.graph.GraphBasedSimilarRefactoringFinder;

public class GraphVsSourcererCCExperiment {

	public static void main(String[] args) throws Exception {
		test();
	}
	
	@Test
	public static void test() throws Exception {
		try {
			Path outputDirectory = Files.createDirectories(Paths.get("E:\\SerializedProjects\\1-200"));
			Path projectsDirectory = Files.createDirectories(Paths.get("E:\\ProjectDataset\\1-200"));
			ExtendedGitService gitService = new ExtendedGitServiceImpl();

			ArrayList<GithubProject> projects = new ArrayList<GithubProject>();
			ArrayList<Pair<RefactoringData, Repository>> refactorings = new ArrayList<Pair<RefactoringData, Repository>>();
			 

			for (String projectLink : projectLinks) {
				GithubProject project;
				try {
 					project = new GithubProject(projectLink, projectsDirectory, outputDirectory, gitService);
					project.printReport();
					projects.add(project);
					for (RefactoringData refactoring : project.getRefactorings()) {
						Pair<RefactoringData, Repository> pair = Pair.of(refactoring, project.getRepository());
						refactorings.add(pair);
					}		
				} catch (Exception e) {
					try(FileWriter fw = new FileWriter("log.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)){
						out.println(e.getStackTrace());
					} catch (IOException ex) {
					}
				}
			}
			
		//	SimilarRefactoringFinder patternFinder = new TokenBasedSimilarRefactoringFinder();

		//	List<RefactoringPair> similarRefactoringPairs = patternFinder.getSimilarRefactoringPairs(refactorings);
			
			GraphBasedSimilarRefactoringFinder gbsrf = new GraphBasedSimilarRefactoringFinder();

	/*		List<RefactoringPair> similarRefactoringPairsByGraphMatching = gbsrf.getSimilarRefactoringPairs(refactorings, gitService);
			
			for (RefactoringPair refactoringPair : similarRefactoringPairsByGraphMatching) {
				System.out.println(refactoringPair.toString());
				System.out.println(refactoringPair.getRefactoringOne().getRefactoredCode().getText());
				System.out.println(refactoringPair.getRefactoringTwo().getRefactoredCode().getText());
			}*/
			assertEquals(true, true);
		} catch (

		IOException e) {
			e.printStackTrace();
			fail("Exception Thrown");
		}
	}
	
	private List<RefactoringPair> loadSerializedRefactoringPairs() throws Exception {
		
		List<RefactoringPair> refactoringPairs = new ArrayList<RefactoringPair>();
		
		String outputPathString = "refactoringpairs";
		if (Files.exists(Paths.get(outputPathString))) {
			FileInputStream fis = new FileInputStream(outputPathString);
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {
				refactoringPairs = (List<RefactoringPair>) ois.readObject();
			} catch (Exception e) {
				System.out.println(e);
			}
			ois.close();
		} else {
		}
		return refactoringPairs;
	}
	
	private void writeToFile(List<RefactoringPair> refactoringPairs) throws IOException {
		Files.deleteIfExists(Paths.get("refactoringpairs"));
		FileOutputStream fos = new FileOutputStream("refactoringpairs");
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactoringPairs);
			oos.close();
		} catch (IOException e) {
			System.out.println("Failed to serialize");
			System.out.println(e);
		}

	}

	static String[] projectLinks = {
/*			"https://github.com/iluwatar/java-design-patterns",
			"https://github.com/ReactiveX/RxJava",

			//			"https://github.com/elastic/elasticsearch", incomplete
			
			"https://github.com/square/retrofit",
			"https://github.com/square/okhttp",*/
			
//		    "https://github.com/spring-projects/spring-boot",
//			"https://github.com/google/guava", incomplete
//			"https://github.com/kdn251/interviews", incomplete
			
			"https://github.com/PhilJay/MPAndroidChart",
			
//			"https://github.com/bumptech/glide", incomplete
//			"https://github.com/spring-projects/spring-framework", incomplete
/*			"https://github.com/JakeWharton/butterknife",
			"https://github.com/airbnb/lottie-android",
			"https://github.com/square/leakcanary",
			"https://github.com/apache/incubator-dubbo",
			"https://github.com/zxing/zxing",*/
		/*	"https://github.com/greenrobot/EventBus",
			"https://github.com/Blankj/AndroidUtilCode",
			"https://github.com/nostra13/Android-Universal-Image-Loader",
			"https://github.com/ReactiveX/RxAndroid",
			"https://github.com/google/iosched",
			"https://github.com/square/picasso",
			"https://github.com/skylot/jadx",
			"https://github.com/facebook/fresco",
			"https://github.com/netty/netty",
			"https://github.com/libgdx/libgdx",
			"https://github.com/Netflix/Hystrix",
			"https://github.com/alibaba/fastjson",
			"https://github.com/CymChad/BaseRecyclerViewAdapterHelper",
			"https://github.com/afollestad/material-dialogs",
			"https://github.com/chrisbanes/PhotoView",
			"https://github.com/scwang90/SmartRefreshLayout",
			"https://github.com/Tencent/tinker",
//			"https://github.com/android10/Android-CleanArchitecture", incomplete
			"https://github.com/lgvalle/Material-Animations",
			"https://github.com/alibaba/druid",
			"https://github.com/nickbutcher/plaid",
			"https://github.com/SeleniumHQ/selenium",
			"https://github.com/loopj/android-async-http",
			"https://github.com/shuzheng/zheng",
			"https://github.com/google/ExoPlayer",
			"https://github.com/daimajia/AndroidSwipeLayout",
			"https://github.com/greenrobot/greenDAO",
			"https://github.com/hdodenhof/CircleImageView",
//			"https://github.com/DrKLO/Telegram", incomplete
			"https://github.com/facebook/stetho",
//			"https://github.com/signalapp/Signal-Android", incomplete
			
			"https://github.com/LyndonChin/MasteringAndroidDataBinding",
			"https://github.com/go-lang-plugin-org/go-lang-idea-plugin",
			"https://github.com/grpc/grpc-java",
			"https://github.com/TheAlgorithms/Java",

//			"https://github.com/realm/realm-java", stuck at commit d9e0ae4ac083286c5440cc4bdb0f18d9c70fdb45
			"https://github.com/apache/incubator-weex",
			"https://github.com/daimajia/AndroidViewAnimations",
			"https://github.com/googlesamples/android-UniversalMusicPlayer",
			"https://github.com/Konloch/bytecode-viewer",
			"https://github.com/winterbe/java8-tutorial",
//			"https://github.com/pockethub/PocketHub", error
			"https://github.com/mikepenz/MaterialDrawer",
			"https://github.com/EnterpriseQualityCoding/FizzBuzzEnterpriseEdition",
			"https://github.com/orhanobut/logger",
			"https://github.com/liaohuqiu/android-Ultra-Pull-To-Refresh",
			"https://github.com/bazelbuild/bazel",
			"https://github.com/deeplearning4j/deeplearning4j",
			"https://github.com/proxyee-down-org/proxyee-down",
			"https://github.com/navasmdc/MaterialDesignLibrary",
//			"https://github.com/openzipkin/zipkin", error
*/			
			
			
			
			
			
			
			
			
			
			
			
/*			"https://github.com/apache/kafka",
			"https://github.com/wasabeef/recyclerview-animators",
			"https://github.com/alibaba/vlayout",
			"https://github.com/Tencent/VasSonic",
			"https://github.com/eclipse/vert.x",
			"https://github.com/HannahMitt/HomeMirror",
			"https://github.com/LMAX-Exchange/disruptor",
			"https://github.com/chrisjenx/Calligraphy",
			"https://github.com/apl-devs/AppIntro",
			"https://github.com/Curzibn/Luban",
			"https://github.com/umano/AndroidSlidingUpPanel",
			"https://github.com/aosp-mirror/platform_frameworks_base",
			"https://github.com/mybatis/mybatis-3",
			"https://github.com/prestodb/presto",
			"https://github.com/JakeWharton/RxBinding",
			"https://github.com/chrisbanes/cheesesquare",
			"https://github.com/perwendel/spark",
			"https://github.com/81813780/AVLoadingIndicatorView",
			"https://github.com/wix/react-native-navigation",
			"https://github.com/Bigkoo/Android-PickerView",
			"https://github.com/florent37/MaterialViewPager",
			"https://github.com/permissions-dispatcher/PermissionsDispatcher",
			"https://github.com/JackyAndroid/AndroidInterview-Q-A",
			"https://github.com/apache/hadoop",
			"https://github.com/hankcs/HanLP",
			"https://github.com/clojure/clojure",
			"https://github.com/Yalantis/uCrop",
			"https://github.com/google/agera",
			"https://github.com/zhihu/Matisse",
			"https://github.com/brettwooldridge/HikariCP",
			"https://github.com/lipangit/JiaoZiVideoPlayer",
			"https://github.com/junit-team/junit4",
			"https://github.com/Bilibili/DanmakuFlameMaster",
			"https://github.com/mockito/mockito",
			"https://github.com/square/dagger",
			"https://github.com/google/guice",
			"https://github.com/lingochamp/FileDownloader",
			"https://github.com/Bearded-Hen/Android-Bootstrap",
			"https://github.com/xetorthio/jedis",
			"https://github.com/google/auto",
			"https://github.com/cymcsg/UltimateRecyclerView",
			"https://github.com/dropwizard/dropwizard",
			"https://github.com/code4craft/webmagic",
			"https://github.com/druid-io/druid",
			"https://github.com/Netflix/SimianArmy",
			"https://github.com/H07000223/FlycoTabLayout",
			"https://github.com/YoKeyword/Fragmentation",
			"https://github.com/vondear/RxTools",
			"https://github.com/tbruyelle/RxPermissions",
			"https://github.com/iBotPeaches/Apktool",
			"https://github.com/rzwitserloot/lombok",
			"https://github.com/wasabeef/glide-transformations",
			"https://github.com/koral--/android-gif-drawable",
			"https://github.com/naver/pinpoint",
			"https://github.com/alibaba/atlas",
			"https://github.com/geeeeeeeeek/WeChatLuckyMoney",
			"https://github.com/youth5201314/banner",
			"https://github.com/CarGuo/GSYVideoPlayer",
			"https://github.com/JetBrains/intellij-community",
			"https://github.com/alibaba/ARouter",
			"https://github.com/jhy/jsoup",
			"https://github.com/aritraroy/UltimateAndroidReference",
			"https://github.com/eugenp/tutorials",
			"https://github.com/lecho/hellocharts-android",
			"https://github.com/pedant/sweet-alert-dialog",
			"https://github.com/facebook/buck",
			"https://github.com/didi/VirtualAPK",
			"https://github.com/dropwizard/metrics",
			"https://github.com/koush/ion",
			"https://github.com/ChrisRM/material-theme-jetbrains",
			"https://github.com/trello/RxLifecycle",
			"https://github.com/JakeWharton/timber",
			"https://github.com/googlesamples/easypermissions",
			"https://github.com/futuresimple/android-floating-action-button",
			"https://github.com/rey5137/material",
			"https://github.com/laobie/StatusBarUtil",
			"https://github.com/Netflix/eureka",
			"https://github.com/react-native-community/react-native-camera",
			"https://github.com/googlesamples/android-testing",
			"https://github.com/square/javapoet",
			"https://github.com/springside/springside4",
			"https://github.com/JessYanCoding/MVPArms",
			"https://github.com/emilsjolander/StickyListHeaders",
			"https://github.com/OpenRefine/OpenRefine",
			"https://github.com/amlcurran/ShowcaseView",
			"https://github.com/redisson/redisson",
			"https://github.com/daniulive/SmarterStreaming",
			"https://github.com/antoniolg/androidmvp",
			"https://github.com/GcsSloop/AndroidNote",
			"https://github.com/rengwuxian/MaterialEditText",
			"https://github.com/dbeaver/dbeaver",
			"https://github.com/ctripcorp/apollo",
			"https://github.com/google/j2objc",
			"https://github.com/neo4j/neo4j",
			"https://github.com/square/otto",
			"https://github.com/ikew0ng/SwipeBackLayout",
			"https://github.com/facebook/rebound",
			"https://github.com/apache/storm",
			"https://github.com/swagger-api/swagger-core",
			"https://github.com/crazycodeboy/TakePhoto",
			"https://github.com/wyouflf/xUtils3",
			"https://github.com/facebook/litho",
			"https://github.com/QMUI/QMUI_Android",
			"https://github.com/daimajia/NumberProgressBar",
			"https://github.com/Netflix/zuul",
			"https://github.com/oracle/graal",
			"https://github.com/google/android-classyshark",
			"https://github.com/Devlight/InfiniteCycleViewPager",
			"https://github.com/dmytrodanylyk/circular-progress-button",
			"https://github.com/lzyzsd/JsBridge",
			"https://github.com/sparklemotion/nokogiri",
			"https://github.com/LitePalFramework/LitePal",
			"https://github.com/stanfordnlp/CoreNLP",
			"https://github.com/eclipse/che",
			"https://github.com/leolin310148/ShortcutBadger",
			"https://github.com/JeffLi1993/springboot-learning-example",
			"https://github.com/gabrielemariotti/cardslib",
			"https://github.com/mission-peace/interview",
			"https://github.com/alibaba/canal",
			"https://github.com/shwenzhang/AndResGuard",
			"https://github.com/ybq/Android-SpinKit",
			"https://github.com/naman14/Timber",
			"https://github.com/etsy/AndroidStaggeredGrid",
			"https://github.com/Nightonke/BoomMenu",
			"https://github.com/traex/RippleEffect",
			"https://github.com/square/sqlbrite",
			"https://github.com/Yalantis/Side-Menu.Android",
			"https://github.com/Qihoo360/RePlugin",
			"https://github.com/pxb1988/dex2jar",
			"https://github.com/alibaba/freeline",
			"https://github.com/kickstarter/android-oss",
			"https://github.com/pardom-zz/ActiveAndroid",
			"https://github.com/sockeqwe/mosby",
			"https://github.com/b3log/solo",
			"https://github.com/izzyleung/ZhihuDailyPurify",
			"https://github.com/facebook/facebook-android-sdk",
			"https://github.com/commonsguy/cw-omnibus", */
	};

}
