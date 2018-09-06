package ca.concordia.refactoringmatcher;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.concordia.refactoringdata.IRefactoringData;
import ca.concordia.refactoringmatcher.graph.GraphBasedSimilarRefactoringFinder;

public class RefactoringMatcher {
	private static Logger logger;
	public static void main(String[] args) throws Exception {
		test();
	}
	
	public static void test() {
		try {
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(Calendar.getInstance().getTime());
			System.setProperty("alllogfilename", "E:\\Output\\Logs\\" + timeStamp + ".log");
			System.setProperty("criticallogfilename", "E:\\Output\\Logs\\" + timeStamp + "_Critical.log");
			logger = LoggerFactory.getLogger(RefactoringMatcher.class);
			Path outputDirectory = Files.createDirectories(Paths.get("E:\\Output\\SerializedRepositories"));
			Path projectsDirectory = Files.createDirectories(Paths.get("E:\\Output\\Repositories"));
			String refactoringDataPath = "E:\\Output\\SerializedRefactoringData";

			ArrayList<GitProject> projects = new ArrayList<GitProject>();
			ArrayList<IRefactoringData> refactorings = new ArrayList<IRefactoringData>();

			if (Files.exists(Paths.get(refactoringDataPath))) {
				FileInputStream fis = new FileInputStream(refactoringDataPath);
				ObjectInputStream ois = new ObjectInputStream(fis);
				try {
					refactorings = (ArrayList<IRefactoringData>) ois.readObject();
					for (IRefactoringData iRefactoringData : refactorings) {
						iRefactoringData.recoverAfterDeserialization();
					}
				} catch (Exception e) {
					logger.error("Could not load serialized object: " + refactoringDataPath);
					ois.close();
					fis.close();
				}
				ois.close();
				fis.close();
			} else {
				for (String projectLink : projectLinks) {
					GitProject project;
					try {
						project = new GitProject(projectLink, projectsDirectory, outputDirectory);
						project.printReport();
						projects.add(project);
						refactorings.addAll(project.getAllRefactoringData());
					} catch (Exception e) {
						logger.error("Error analyzing project: " + projectLink);
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
				serializeAllRefactoringData(refactorings);
			}
			
			GraphBasedSimilarRefactoringFinder gbsrf = new GraphBasedSimilarRefactoringFinder();
			List<RefactoringPair> similarRefactoringPairs = gbsrf.getSimilarRefactoringPairs(refactorings);
			
//			SimilarRefactoringFinder patternFinder = new TokenBasedSimilarRefactoringFinder();
//			List<RefactoringPair> similarRefactoringPairs = patternFinder	.getSimilarRefactoringPairs(refactorings);

			List<RefactoringPair> interProject = new ArrayList<>();
			
			for (RefactoringPair refactoringPair : similarRefactoringPairs) {
				if(!refactoringPair.fromSameProject()) {
					interProject.add(refactoringPair);
					logger.info(refactoringPair.toString());
				}
			}
			logger.info("Matches: " + similarRefactoringPairs.size());
			logger.info("Matches: " + interProject.size());

			assertEquals(true, true);
		} catch (
		IOException e) {
			logger.error(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}
	
	private static void serializeAllRefactoringData (List<IRefactoringData> refactorings) throws IOException {
		Files.deleteIfExists(Paths.get("E:\\Output\\SerializedRefactoringData"));
		FileOutputStream fos = new FileOutputStream("E:\\Output\\SerializedRefactoringData");
		ObjectOutputStream oos = null;
		try {
			for (IRefactoringData iRefactoringData : refactorings) {
				iRefactoringData.prepareForSerialization();
			}
			oos = new ObjectOutputStream(fos);
			oos.writeObject(refactorings);
			oos.close();
			fos.close();
			
			for (IRefactoringData iRefactoringData : refactorings) {
				iRefactoringData.recoverAfterDeserialization();
			}
		} catch (IOException e) {
			logger.error(e.getStackTrace().toString());
			logger.error("Failed to serialize");
			oos.close();
			fos.close();
			Files.deleteIfExists(Paths.get(("E:\\Output\\SerializedRefactoringData")));
		}
	}

	static String[] projectLinks = {
			
			"https://github.com/iluwatar/java-design-patterns",
			"https://github.com/ReactiveX/RxJava",
       		"https://github.com/elastic/elasticsearch",
			"https://github.com/square/retrofit",
     		"https://github.com/square/okhttp",
			"https://github.com/spring-projects/spring-boot",
			"https://github.com/google/guava",
			"https://github.com/PhilJay/MPAndroidChart",
			"https://github.com/bumptech/glide",
			"https://github.com/spring-projects/spring-framework",
			"https://github.com/JakeWharton/butterknife",
			"https://github.com/airbnb/lottie-android",
			"https://github.com/square/leakcanary",
			"https://github.com/apache/incubator-dubbo",
			"https://github.com/zxing/zxing",
			"https://github.com/Blankj/AndroidUtilCode",
			"https://github.com/nostra13/Android-Universal-Image-Loader",
			"https://github.com/ReactiveX/RxAndroid",
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
			"https://github.com/Tencent/tinker",
			"https://github.com/alibaba/druid",
			"https://github.com/SeleniumHQ/selenium",
			"https://github.com/loopj/android-async-http",
			"https://github.com/google/ExoPlayer",
			"https://github.com/daimajia/AndroidSwipeLayout",
			"https://github.com/greenrobot/greenDAO",
			"https://github.com/hdodenhof/CircleImageView",
			"https://github.com/facebook/stetho",
			"https://github.com/signalapp/Signal-Android",
			"https://github.com/realm/realm-java",
			"https://github.com/apache/incubator-weex",
			"https://github.com/daimajia/AndroidViewAnimations",
			"https://github.com/Konloch/bytecode-viewer",
			"https://github.com/pockethub/PocketHub",
			"https://github.com/mikepenz/MaterialDrawer",
			"https://github.com/orhanobut/logger",
			"https://github.com/bazelbuild/bazel",
			"https://github.com/deeplearning4j/deeplearning4j",
			"https://github.com/openzipkin/zipkin",
			"https://github.com/apache/kafka", 
			
			
			"https://github.com/Netflix/eureka",
			"https://github.com/react-native-community/react-native-camera",
			"https://github.com/square/javapoet",
			"https://github.com/JessYanCoding/MVPArms",
			"https://github.com/emilsjolander/StickyListHeaders",
			"https://github.com/OpenRefine/OpenRefine",
			"https://github.com/amlcurran/ShowcaseView",
			"https://github.com/redisson/redisson",
			"https://github.com/rengwuxian/MaterialEditText",
			"https://github.com/dbeaver/dbeaver",
			"https://github.com/ctripcorp/apollo",
			"https://github.com/google/j2objc",
			"https://github.com/neo4j/neo4j",
			"https://github.com/square/otto",
			"https://github.com/facebook/rebound",
			"https://github.com/apache/storm",
			"https://github.com/swagger-api/swagger-core",
			"https://github.com/crazycodeboy/TakePhoto",
			"https://github.com/facebook/litho",
			"https://github.com/Netflix/zuul",
			"https://github.com/oracle/graal",
			"https://github.com/google/android-classyshark",
			"https://github.com/sparklemotion/nokogiri",
			"https://github.com/eclipse/che",
			"https://github.com/gabrielemariotti/cardslib",
			"https://github.com/alibaba/canal",
			"https://github.com/shwenzhang/AndResGuard",
			"https://github.com/square/sqlbrite",
			"https://github.com/pxb1988/dex2jar",
			"https://github.com/alibaba/freeline",
			"https://github.com/kickstarter/android-oss",
			"https://github.com/sockeqwe/mosby",
			"https://github.com/b3log/solo",
			"https://github.com/facebook/facebook-android-sdk",
			"https://github.com/commonsguy/cw-omnibus",
			"https://github.com/sharding-sphere/sharding-sphere",
			"https://github.com/go-lang-plugin-org/go-lang-idea-plugin",
			"https://github.com/apache/zookeeper",
			"https://github.com/codecentric/spring-boot-admin",
			"https://github.com/guardianproject/haven",
			"https://github.com/scribejava/scribejava",
			"https://github.com/AsyncHttpClient/async-http-client",
			"https://github.com/apache/cassandra",
			"https://github.com/medcl/elasticsearch-analysis-ik",
			"https://github.com/evernote/android-job",
			"https://github.com/ACRA/acra",
			"https://github.com/pagehelper/Mybatis-PageHelper",
			"https://github.com/grpc/grpc-java",
			"https://github.com/weibocom/motan",
			"https://github.com/MyCATApache/Mycat-Server",
			"https://github.com/xuxueli/xxl-job",
			"https://github.com/GrenderG/Toasty",
			"https://github.com/prolificinteractive/material-calendarview",
			"https://github.com/Graylog2/graylog2-server",
			"https://github.com/processing/processing",
			"https://github.com/Raizlabs/DBFlow",
			"https://github.com/google/closure-compiler",
			"https://github.com/square/android-times-square",
			"https://github.com/wequick/Small",
			"https://github.com/apereo/cas",
			"https://github.com/gocd/gocd",
			"https://github.com/davemorrissey/subsampling-scale-image-view",
			"https://github.com/jdamcd/android-crop",
			"https://github.com/ben-manes/caffeine",
			"https://github.com/h6ah4i/android-advancedrecyclerview",
			"https://github.com/TooTallNate/Java-WebSocket",
			"https://github.com/antlr/antlr4",
			"https://github.com/robolectric/robolectric",
			"https://github.com/zaproxy/zaproxy",
			"https://github.com/java-native-access/jna",
			"https://github.com/react-community/react-native-image-picker",
			"https://github.com/LuckSiege/PictureSelector",
			"https://github.com/square/moshi",
			"https://github.com/orhanobut/dialogplus",
			"https://github.com/andkulikov/Transitions-Everywhere",
			"https://github.com/k9mail/k-9",
			"https://github.com/haifengl/smile",
			"https://github.com/airbnb/epoxy",
			"https://github.com/joelittlejohn/jsonschema2pojo",
			"https://github.com/Flipboard/bottomsheet",
			"https://github.com/Justson/AgentWeb",
			"https://github.com/KeepSafe/TapTargetView",
			"https://github.com/NanoHttpd/nanohttpd",
			
			
			"https://github.com/ArthurHub/Android-Image-Cropper",
			"https://github.com/Activiti/Activiti",
			"https://github.com/jpush/aurora-imui",
			"https://github.com/wdullaer/MaterialDateTimePicker",
			"https://github.com/apache/incubator-skywalking",
			"https://github.com/orfjackal/retrolambda",
			"https://github.com/real-logic/aeron",
			"https://github.com/pili-engineering/PLDroidPlayer",
			"https://github.com/johncarl81/parceler",
			"https://github.com/JetBrains/ideavim",
			"https://github.com/Karumi/Dexter",
			"https://github.com/CellularPrivacy/Android-IMSI-Catcher-Detector",
			"https://github.com/k0shk0sh/FastHub",
			"https://github.com/jwtk/jjwt",
			"https://github.com/drakeet/MultiType",
			"https://github.com/apache/incubator-heron",
			"https://github.com/hibernate/hibernate-orm",
			"https://github.com/Ashok-Varma/BottomNavigation",
			"https://github.com/Atmosphere/atmosphere",
			"https://github.com/Meituan-Dianping/walle",
			"https://github.com/bluelinelabs/LoganSquare",
			"https://github.com/puniverse/quasar",
			"https://github.com/h2oai/h2o-3",
			"https://github.com/NYTimes/Store",
			"https://github.com/rest-assured/rest-assured",
			"https://github.com/flyway/flyway",
			"https://github.com/OpenTSDB/opentsdb",
			"https://github.com/stephanenicolas/robospice",
			"https://github.com/b3log/symphony",
			"https://github.com/bluelinelabs/Conductor",
			"https://github.com/OpenFeign/feign",
			"https://github.com/lsjwzh/RecyclerViewPager",
			"https://github.com/socketio/socket.io-client-java",
			"https://github.com/siacs/Conversations",
			"https://github.com/react-native-community/react-native-video",
			"https://github.com/Microsoft/malmo",
			"https://github.com/ximsfei/Android-skin-support",
			"https://github.com/romandanylyk/PageIndicatorView",
			"https://github.com/Jacksgong/JKeyboardPanelSwitch",
			"https://github.com/orhanobut/hawk",
			"https://github.com/spring-projects/spring-security-oauth",
			"https://github.com/jersey/jersey",
			"https://github.com/firebase/FirebaseUI-Android",
			"https://github.com/mrniko/netty-socketio",
			"https://github.com/JesusFreke/smali",
			"https://github.com/journeyapps/zxing-android-embedded",
			"https://github.com/Meituan-Dianping/Robust",
			"https://github.com/gephi/gephi",
			"https://github.com/json-path/JsonPath",
			"https://github.com/SonarSource/sonarqube",
			"https://github.com/afollestad/material-camera",
			"https://github.com/pushtorefresh/storio",
			"https://github.com/bingoogolapple/BGABanner-Android",
			"https://github.com/apache/groovy",
			"https://github.com/davideas/FlexibleAdapter",
			"https://github.com/hazelcast/hazelcast",
			"https://github.com/bitcoinj/bitcoinj",
			"https://github.com/owncloud/android",
			"https://github.com/vespa-engine/vespa",
			"https://github.com/react-native-community/react-native-svg",
			"https://github.com/requery/requery",
			"https://github.com/googlemaps/android-maps-utils",
			"https://github.com/aws/aws-sdk-java",
			"https://github.com/spring-projects/spring-security",
			"https://github.com/jfinal/jfinal",
			"https://github.com/vavr-io/vavr",
			"https://github.com/bytedeco/javacv",
			"https://github.com/reactive-streams/reactive-streams-jvm",
			"https://github.com/balysv/material-menu",
			"https://github.com/TommyLemon/Android-ZBLibrary",
			"https://github.com/bytedeco/javacpp",
			"https://github.com/jOOQ/jOOQ",
			"https://github.com/termux/termux-app",
			"https://github.com/mybatis/generator",
			"https://github.com/square/wire",
			"https://github.com/eirslett/frontend-maven-plugin",
			"https://github.com/MovingBlocks/Terasology",
			"https://github.com/grails/grails-core",
			"https://github.com/keycloak/keycloak",
			"https://github.com/BelooS/ChipsLayoutManager",
			"https://github.com/apache/tomcat",
			"https://github.com/bisq-network/bisq-desktop",
			"https://github.com/spring-cloud/spring-cloud-netflix",
			"https://github.com/zouzg/mybatis-generator-gui",
			"https://github.com/jd-alexander/LikeButton",
			"https://github.com/knowm/XChange",
			"https://github.com/apache/cordova-android",
			"https://github.com/rstudio/rstudio",
			"https://github.com/MinecraftForge/MinecraftForge",
			"https://github.com/nisrulz/sensey",
			"https://github.com/JakeWharton/Telecine",
			"https://github.com/lygttpod/SuperTextView",
			"https://github.com/graphql-java/graphql-java",
			"https://github.com/mikepenz/FastAdapter",
			"https://github.com/yusuke/twitter4j",
			"https://github.com/jhalterman/failsafe",
			"https://github.com/dlew/joda-time-android",
			"https://github.com/qiujuer/Genius-Android",
			"https://github.com/cryptomator/cryptomator",
			"https://github.com/chewiebug/GCViewer",
			"https://github.com/MagicMashRoom/SuperCalendar",
			"https://github.com/dreamhead/moco",
			"https://github.com/junit-team/junit5",
			"https://github.com/Netflix/ribbon",
			"https://github.com/voldemort/voldemort",
			"https://github.com/mikepenz/AboutLibraries",
			"https://github.com/bigbluebutton/bigbluebutton",
			"https://github.com/chanjarster/weixin-java-tools",
			"https://github.com/Jasonchenlijian/FastBle",
			"https://github.com/raphw/byte-buddy",
			"https://github.com/ZieIony/Carbon",
			"https://github.com/brianfrankcooper/YCSB",
			"https://github.com/Freeyourgadget/Gadgetbridge",
			"https://github.com/spring-projects/spring-loaded",
			"https://github.com/rovo89/XposedInstaller",
			"https://github.com/crate/crate",
			"https://github.com/jjoe64/GraphView",
			"https://github.com/oracle/opengrok",
			"https://github.com/cglib/cglib",
			"https://github.com/eclipse/jetty.project",
			"https://github.com/wildfly/wildfly",
			"https://github.com/languagetool-org/languagetool",
			"https://github.com/immutables/immutables",
			"https://github.com/skavanagh/KeyBox",
			"https://github.com/react-native-community/react-native-linear-gradient",
			"https://github.com/mongodb/mongo-java-driver",
			"https://github.com/undertow-io/undertow",
			
			
			
			"https://github.com/wasabeef/recyclerview-animators",
			"https://github.com/alibaba/vlayout",
			"https://github.com/eclipse/vert.x",
			"https://github.com/LMAX-Exchange/disruptor",
			"https://github.com/aosp-mirror/platform_frameworks_base",
			"https://github.com/mybatis/mybatis-3",
			"https://github.com/prestodb/presto",
			"https://github.com/perwendel/spark",
			"https://github.com/wix/react-native-navigation",
			"https://github.com/florent37/MaterialViewPager",
			"https://github.com/permissions-dispatcher/PermissionsDispatcher",
			"https://github.com/apache/hadoop",
			"https://github.com/hankcs/HanLP",
			"https://github.com/clojure/clojure",
			"https://github.com/google/agera",
			"https://github.com/brettwooldridge/HikariCP",
			"https://github.com/lipangit/JiaoZiVideoPlayer",
			"https://github.com/junit-team/junit4",
			"https://github.com/Bilibili/DanmakuFlameMaster",
			"https://github.com/mockito/mockito",
			"https://github.com/square/dagger",
			"https://github.com/google/guice",
			"https://github.com/lingochamp/FileDownloader",
			"https://github.com/xetorthio/jedis",
			"https://github.com/google/auto",
			"https://github.com/cymcsg/UltimateRecyclerView",
			"https://github.com/dropwizard/dropwizard",
			"https://github.com/code4craft/webmagic",
			"https://github.com/druid-io/druid",
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
			"https://github.com/CarGuo/GSYVideoPlayer",
			"https://github.com/alibaba/ARouter",
			"https://github.com/jhy/jsoup",
			"https://github.com/lecho/hellocharts-android",
			"https://github.com/facebook/buck",
			"https://github.com/dropwizard/metrics",
			"https://github.com/ChrisRM/material-theme-jetbrains",
			"https://github.com/trello/RxLifecycle",
			"https://github.com/JakeWharton/timber",
			"https://github.com/futuresimple/android-floating-action-button",
			
			
			"https://github.com/apache/lucene-solr",
			"https://github.com/notnoop/java-apns",
			"https://github.com/Red5/red5-server",
			"https://github.com/yanzhenjie/Album",
			"https://github.com/jitsi/jitsi",
			"https://github.com/timusus/Shuttle",
			"https://github.com/thingsboard/thingsboard",
			"https://github.com/graphhopper/graphhopper",
			"https://github.com/lisawray/groupie",
			"https://github.com/floragunncom/search-guard",
			"https://github.com/hidroh/materialistic",
			"https://github.com/pbreault/adb-idea",
			"https://github.com/firebase/firebase-jobdispatcher-android",
			"https://github.com/igniterealtime/Openfire",
			"https://github.com/pmd/pmd",
			"https://github.com/fabric8io/fabric8",
			"https://github.com/DreaminginCodeZH/MaterialProgressBar",
			"https://github.com/ethereum/ethereumj",
			"https://github.com/redsolution/xabber-android",
			"https://github.com/square/assertj-android",
			"https://github.com/alibaba/Tangram-Android",
			"https://github.com/tdebatty/java-string-similarity",
			"https://github.com/medyo/android-about-page",
			"https://github.com/jboss-javassist/javassist",
			"https://github.com/Athou/commafeed",
			"https://github.com/vert-x3/vertx-examples",
			"https://github.com/oney/react-native-webrtc",
			"https://github.com/M66B/NetGuard",
			"https://github.com/akexorcist/Android-RoundCornerProgressBar",
			"https://github.com/jamesdbloom/mockserver",
			"https://github.com/mancj/SlideUp-Android",
			"https://github.com/spotify/docker-maven-plugin",
			"https://github.com/tipsy/javalin",
			"https://github.com/sqlcipher/android-database-sqlcipher",
			"https://github.com/reactor/reactor-core",
			"https://github.com/stephentuso/welcome-android",
			"https://github.com/razerdp/BasePopup",
			"https://github.com/parse-community/Parse-SDK-Android",
			"https://github.com/janishar/PlaceHolderView",
			"https://github.com/zeromq/jeromq",
			"https://github.com/groovy/groovy-core",
			"https://github.com/deeplearning4j/nd4j",
			"https://github.com/apache/kylin",
			"https://github.com/medyo/Fancybuttons",
			"https://github.com/commonsguy/cw-advandroid",
			
			
			
			
			
			
			
			
		/*	"https://github.com/apache/flink",
		  	"https://github.com/lets-blade/blade",
			"https://github.com/mikepenz/Android-Iconics",
			"https://github.com/google/error-prone",
			"https://github.com/checkstyle/checkstyle",
			"https://github.com/NLPchina/elasticsearch-sql",
			"https://github.com/TeamNewPipe/NewPipe",
			"https://github.com/gzu-liyujiang/AndroidPicker",
			"https://github.com/mcxiaoke/packer-ng-plugin",
			"https://github.com/elasticjob/elastic-job-lite",
			"https://github.com/apache/zeppelin",
			"https://github.com/JodaOrg/joda-time",
			"https://github.com/lightbend/config",
			"https://github.com/Alluxio/alluxio",
			"https://github.com/orientechnologies/orientdb",
			"https://github.com/alibaba/jstorm",
			"https://github.com/openhab/openhab1-addons",
			
			
			"https://github.com/BuildCraft/BuildCraft",
			"https://github.com/couchbase/couchbase-lite-android",
			"https://github.com/docker-java/docker-java",
			"https://github.com/lyft/scoop",
			"https://github.com/open-keychain/open-keychain",
			"https://github.com/eneim/toro",
			"https://github.com/ReactiveX/RxNetty",
			"https://github.com/sockeqwe/fragmentargs",
			"https://github.com/gwtproject/gwt",
			"https://github.com/jdbi/jdbi",
			"https://github.com/apache/curator",
			"https://github.com/kohsuke/jenkins",
			"https://github.com/simpligility/android-maven-plugin",
			"https://github.com/bpellin/keepassdroid",
			"https://github.com/michael-rapp/ChromeLikeTabSwitcher",
			"https://github.com/qos-ch/slf4j",
			"https://github.com/dcevm/dcevm",
			"https://github.com/graknlabs/grakn",
			"https://github.com/sjwall/MaterialTapTargetPrompt",
			"https://github.com/Netflix/astyanax",
			"https://github.com/ParkSangGwon/TedPermission",
			"https://github.com/julian-klode/dns66",
			"https://github.com/logstash/logstash-logback-encoder",
			"https://github.com/rubenlagus/TelegramBots",
			"https://github.com/HotswapProjects/HotswapAgent",
			"https://github.com/blynkkk/blynk-server",
			"https://github.com/java-json-tools/json-schema-validator",
			"https://github.com/huanghongxun/HMCL",
			"https://github.com/RuedigerMoeller/fast-serialization",
			"https://github.com/apache/drill",
			"https://github.com/TakahikoKawasaki/nv-websocket-client",
			"https://github.com/yjfnypeu/UpdatePlugin",
			"https://github.com/alibaba/transmittable-thread-local",
			"https://github.com/javiersantos/MaterialStyledDialogs",
			"https://github.com/katzer/cordova-plugin-background-mode",
			"https://github.com/atduskgreg/opencv-processing",
			"https://github.com/wildabeast/BarcodeScanner",
			"https://github.com/menacher/java-game-server",
			"https://github.com/apollographql/apollo-android",
			"https://github.com/jtablesaw/tablesaw",
			"https://github.com/apache/nifi",
			"https://github.com/iotaledger/iri",
			"https://github.com/grandcentrix/ThirtyInch",
			"https://github.com/zeapo/Android-Password-Store",
			"https://github.com/jindrapetrik/jpexs-decompiler",
			"https://github.com/ervandew/eclim",
			"https://github.com/TigerVNC/tigervnc",
			"https://github.com/datastax/java-driver",
			"https://github.com/widdix/aws-cf-templates",
			"https://github.com/spotify/docker-client",
			"https://github.com/vrapper/vrapper",
			"https://github.com/daniel-stoneuk/material-about-library",
			"https://github.com/Neamar/KISS",
			"https://github.com/json-iterator/java",
			"https://github.com/KronicDeth/intellij-elixir",
			"https://github.com/strapdata/elassandra",
			"https://github.com/xerial/sqlite-jdbc",
			"https://github.com/mpetazzoni/ttorrent",
			"https://github.com/googlemaps/google-maps-services-java",
			"https://github.com/jaychang0917/SimpleRecyclerView",
			"https://github.com/Sable/soot",
			"https://github.com/JabRef/jabref",
			"https://github.com/ocpsoft/prettytime",
			"https://github.com/zalando/zalenium",
			"https://github.com/apache/avro",
			"https://github.com/plutext/docx4j",
			"https://github.com/ccrama/Slide",
			"https://github.com/westnordost/StreetComplete",
			"https://github.com/kaaproject/kaa",
			"https://github.com/jknack/handlebars.java",
			"https://github.com/MizzleDK/Mizuu",
			"https://github.com/ikarus23/MifareClassicTool",
			"https://github.com/asciidocfx/AsciidocFX",
			"https://github.com/slapperwan/gh4a",
			"https://github.com/PureDark/H-Viewer",
			"https://github.com/SpongePowered/SpongeAPI",
			"https://github.com/konsoletyper/teavm",
			"https://github.com/fabioCollini/DaggerMock",
			"https://github.com/glyptodon/guacamole-client",
			"https://github.com/Netflix/genie",
			"https://github.com/qiujiayu/AutoLoadCache",
			"https://github.com/spring-cloud/spring-cloud-config",
			"https://github.com/runelite/runelite",
			"https://github.com/ISchwarz23/SortableTableView",
			"https://github.com/jhipster/jhipster-sample-app",
			"https://github.com/mabe02/lanterna",
			"https://github.com/yeriomin/YalpStore",
			"https://github.com/dain/leveldb",
			"https://github.com/drewnoakes/metadata-extractor",
			"https://github.com/andsel/moquette",
			"https://github.com/jakob-grabner/Circle-Progress-View",
			"https://github.com/ramswaroop/jbot",
			"https://github.com/syncthing/syncthing-android",
			"https://github.com/resilience4j/resilience4j",
			"https://github.com/recruit-lifestyle/FloatingView",
			"https://github.com/mendhak/gpslogger",
			"https://github.com/apache/usergrid",
			"https://github.com/fossasia/open-event-android",
			"https://github.com/zeroturnaround/zt-zip",
			"https://github.com/apilayer/restcountries",
			"https://github.com/confluentinc/kafka-rest",
			"https://github.com/MEiDIK/SlimAdapter",
			"https://github.com/SilenceIM/Silence",
			"https://github.com/rkalla/imgscalr",
			"https://github.com/tianzhijiexian/CommonAdapter",
			"https://github.com/RoaringBitmap/RoaringBitmap",
			"https://github.com/swaldman/c3p0",
			"https://github.com/amaembo/streamex",
			"https://github.com/msgpack/msgpack-java",
			"https://github.com/spring-projects/spring-data-mongodb",
			"https://github.com/javiersantos/MLManager",
			"https://github.com/Netflix/EVCache",
			"https://github.com/Netflix/EVCache",
			"https://github.com/Netflix/Priam",
			"https://github.com/debezium/debezium",
			"https://github.com/protostuff/protostuff",
			"https://github.com/xdtianyu/CallerInfo",
			"https://github.com/bytedeco/javacpp-presets",
			"https://github.com/hypertrack/smart-scheduler-android",
			"https://github.com/JetBrains/MPS",
			"https://github.com/kiegroup/optaplanner",
			"https://github.com/aol/micro-server",
			"https://github.com/apache/struts",
			"https://github.com/wuxudong/react-native-charts-wrapper",
			"https://github.com/apache/ambari",
			"https://github.com/nordnet/cordova-hot-code-push",
			"https://github.com/QuantumBadger/RedReader",
			"https://github.com/cgeo/cgeo",
			"https://github.com/GoogleCloudPlatform/DataflowJavaSDK",
			"https://github.com/j-easy/easy-rules",
			"https://github.com/sephiroth74/android-target-tooltip",
			"https://github.com/google/google-http-java-client",
			"https://github.com/zhegexiaohuozi/SeimiCrawler",
			"https://github.com/relayrides/pushy",
			"https://github.com/ihsanbal/LoggingInterceptor",
			"https://github.com/ctripcorp/dal",
			"https://github.com/joscha/play-authenticate",
			"https://github.com/micrometer-metrics/micrometer",
			"https://github.com/FlyingPumba/SimpleRatingBar",
			"https://github.com/facebookarchive/nifty",
			"https://github.com/Impetus/Kundera",
			"https://github.com/droidparts/droidparts",
			"https://github.com/topjohnwu/MagiskManager",
			"https://github.com/facebookarchive/swift",
			"https://github.com/flowable/flowable-engine",
			"https://github.com/intuit/karate",
			"https://github.com/medcl/elasticsearch-analysis-pinyin",
			"https://github.com/modelmapper/modelmapper",
			"https://github.com/DiUS/java-faker",
			"https://github.com/linkedin/parseq",
			"https://github.com/chrislacy/TweetLanes",
			"https://github.com/GlowstoneMC/Glowstone",
			"https://github.com/spring-projects/spring-data-redis",
			"https://github.com/orgzly/orgzly-android",
			"https://github.com/codinguser/gnucash-android",
			"https://github.com/openhab/openhab2-addons",
			"https://github.com/kiegroup/jbpm",
			"https://github.com/jooby-project/jooby",
			"https://github.com/ashqal/MD360Player4Android",
			"https://github.com/itext/itextpdf",
			"https://github.com/yacy/yacy_search_server",
			"https://github.com/crashub/crash",
			"https://github.com/oblador/react-native-keychain",
			"https://github.com/codenameone/CodenameOne",
			"https://github.com/networknt/light-4j",
			"https://github.com/hsz/idea-gitignore",
			"https://github.com/nextcloud/android",
			"https://github.com/kmagiera/react-native-gesture-handler",
			"https://github.com/cloudfoundry/uaa",
			"https://github.com/leandroBorgesFerreira/LoadingButtonAndroid",
			"https://github.com/mozilla-mobile/focus-android",
			"https://github.com/zendesk/maxwell",
			"https://github.com/vipshop/Saturn",
			"https://github.com/quran/quran_android",
			"https://github.com/spring-projects/spring-hateoas",
			"https://github.com/twitter/hbc",
			"https://github.com/wikimedia/apps-android-wikipedia",
			"https://github.com/mcxiaoke/Android-Next",
			"https://github.com/spotify/dockerfile-maven",
			"https://github.com/bennidi/mbassador",
			"https://github.com/lenskit/lenskit",
			"https://github.com/atlassian/commonmark-java",
			"https://github.com/naoufal/react-native-touch-id",
			"https://github.com/QuadFlask/colorpicker",
			"https://github.com/rt2zz/react-native-contacts",
			"https://github.com/rnewson/couchdb-lucene",
			"https://github.com/hss01248/DialogUtil",
			"https://github.com/bugsnag/bugsnag-android",
			"https://github.com/codeborne/selenide",
			"https://github.com/aol/cyclops-react",
			"https://github.com/HubSpot/Singularity",
			"https://github.com/spring-projects/spring-integration",
			"https://github.com/google/google-api-java-client",
			"https://github.com/spring-projects/spring-restdocs",
			"https://github.com/opentracing/opentracing-java",
			"https://github.com/npgall/cqengine",
			"https://github.com/apache/calcite",
			"https://github.com/lfkdsk/JustWeEngine",
			"https://github.com/scala-android/sbt-android",
			"https://github.com/shyiko/mysql-binlog-connector-java",*/
	};
}
