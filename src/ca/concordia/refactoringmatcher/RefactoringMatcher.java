package ca.concordia.refactoringmatcher;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
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

			ArrayList<GitProject> projects = new ArrayList<GitProject>();
			ArrayList<IRefactoringData> refactorings = new ArrayList<IRefactoringData>();

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
			
//			GraphBasedSimilarRefactoringFinder gbsrf = new GraphBasedSimilarRefactoringFinder();
//			List<RefactoringPair> similarRefactoringPairs = gbsrf.getSimilarRefactoringPairs(refactorings);
			
//			SimilarRefactoringFinder patternFinder = new TokenBasedSimilarRefactoringFinder();
//			List<RefactoringPair> similarRefactoringPairs = patternFinder
//					.getSimilarRefactoringPairs(project.getRefactorings());

//			List<RefactoringPair> interProject = new ArrayList<>();
//			
//			for (RefactoringPair refactoringPair : similarRefactoringPairs) {
//				if(!refactoringPair.fromSameProject()) {
//					interProject.add(refactoringPair);
//					logger.info(refactoringPair.toString());
//				}
//			}
//			logger.info("Matches: " + similarRefactoringPairs.size());
//			logger.info("Matches: " + interProject.size());

			assertEquals(true, true);
		} catch (
		IOException e) {
			logger.error(e.getStackTrace().toString());
			e.printStackTrace();
		}
	}

	static String[] projectLinks = {
//     		"https://github.com/BuildCraft/BuildCraft", //getCommits error
//			"https://github.com/couchbase/couchbase-lite-android",
//			"https://github.com/docker-java/docker-java",
//			"https://github.com/lyft/scoop",
//			"https://github.com/open-keychain/open-keychain", //Checkout conflict error
//			"https://github.com/eneim/toro",  getCommits error
//			"https://github.com/ReactiveX/RxNetty",  getCommits error
			
//			"https://github.com/sockeqwe/fragmentargs",
//			"https://github.com/gwtproject/gwt",
//			"https://github.com/jdbi/jdbi",
//			"https://github.com/apache/curator",
//			"https://github.com/kohsuke/jenkins",
//			"https://github.com/simpligility/android-maven-plugin",
//			"https://github.com/bpellin/keepassdroid",
//			"https://github.com/michael-rapp/ChromeLikeTabSwitcher",
//			"https://github.com/qos-ch/slf4j",
//			"https://github.com/dcevm/dcevm",
//			"https://github.com/graknlabs/grakn",
//			"https://github.com/sjwall/MaterialTapTargetPrompt",
//			"https://github.com/Netflix/astyanax",
//			"https://github.com/ParkSangGwon/TedPermission",
//			"https://github.com/julian-klode/dns66",
//			"https://github.com/logstash/logstash-logback-encoder",
//			"https://github.com/rubenlagus/TelegramBots",
//			"https://github.com/HotswapProjects/HotswapAgent",
//			"https://github.com/blynkkk/blynk-server",
//			"https://github.com/java-json-tools/json-schema-validator",
//			"https://github.com/huanghongxun/HMCL",
//			"https://github.com/RuedigerMoeller/fast-serialization",
//			"https://github.com/apache/drill",
//			"https://github.com/TakahikoKawasaki/nv-websocket-client",
//			"https://github.com/yjfnypeu/UpdatePlugin",
//			"https://github.com/alibaba/transmittable-thread-local",
//			"https://github.com/javiersantos/MaterialStyledDialogs",
//			"https://github.com/katzer/cordova-plugin-background-mode",
//			"https://github.com/atduskgreg/opencv-processing",
//			"https://github.com/wildabeast/BarcodeScanner",
//			"https://github.com/menacher/java-game-server",
//			"https://github.com/apollographql/apollo-android",
//			"https://github.com/jtablesaw/tablesaw",
//			"https://github.com/apache/nifi",
//			"https://github.com/iotaledger/iri",
//			"https://github.com/grandcentrix/ThirtyInch",
//			"https://github.com/zeapo/Android-Password-Store",
//			"https://github.com/jindrapetrik/jpexs-decompiler",
//			"https://github.com/ervandew/eclim",
//			"https://github.com/TigerVNC/tigervnc",
//			"https://github.com/datastax/java-driver",
//			"https://github.com/widdix/aws-cf-templates",
//			"https://github.com/spotify/docker-client",
//			"https://github.com/vrapper/vrapper",
//			"https://github.com/daniel-stoneuk/material-about-library",
//			"https://github.com/Neamar/KISS",
//			"https://github.com/json-iterator/java",
//			"https://github.com/KronicDeth/intellij-elixir",
//			"https://github.com/strapdata/elassandra",
//			"https://github.com/xerial/sqlite-jdbc",
//			"https://github.com/mpetazzoni/ttorrent",
//			"https://github.com/googlemaps/google-maps-services-java",
//			"https://github.com/jaychang0917/SimpleRecyclerView",
//			"https://github.com/Sable/soot",
//			"https://github.com/JabRef/jabref",
//			"https://github.com/ocpsoft/prettytime",
//			"https://github.com/zalando/zalenium",
//			"https://github.com/apache/avro",
//			"https://github.com/plutext/docx4j",
//			"https://github.com/ccrama/Slide",
//			"https://github.com/westnordost/StreetComplete",
//			"https://github.com/kaaproject/kaa",
//			"https://github.com/jknack/handlebars.java",
//			"https://github.com/MizzleDK/Mizuu",
//			"https://github.com/ikarus23/MifareClassicTool",
//			"https://github.com/asciidocfx/AsciidocFX",
//			"https://github.com/slapperwan/gh4a",
//			"https://github.com/PureDark/H-Viewer",
//			"https://github.com/SpongePowered/SpongeAPI",
//			"https://github.com/konsoletyper/teavm",
//			"https://github.com/fabioCollini/DaggerMock",
//			"https://github.com/glyptodon/guacamole-client",
//			"https://github.com/Netflix/genie",
//			"https://github.com/qiujiayu/AutoLoadCache",
//			"https://github.com/spring-cloud/spring-cloud-config",
//			"https://github.com/runelite/runelite",
//			"https://github.com/ISchwarz23/SortableTableView",
//			"https://github.com/jhipster/jhipster-sample-app",
//			"https://github.com/mabe02/lanterna",
//			"https://github.com/yeriomin/YalpStore",
//			"https://github.com/dain/leveldb",
//			"https://github.com/drewnoakes/metadata-extractor",
//			"https://github.com/andsel/moquette",
//			"https://github.com/jakob-grabner/Circle-Progress-View",
//			"https://github.com/ramswaroop/jbot",
//			"https://github.com/syncthing/syncthing-android",
//			"https://github.com/resilience4j/resilience4j",
//			"https://github.com/recruit-lifestyle/FloatingView",
//			"https://github.com/mendhak/gpslogger",
//			"https://github.com/apache/usergrid",
//			"https://github.com/fossasia/open-event-android",
//			"https://github.com/zeroturnaround/zt-zip",
//			"https://github.com/apilayer/restcountries",
//			"https://github.com/confluentinc/kafka-rest",
//			"https://github.com/MEiDIK/SlimAdapter",
//			"https://github.com/SilenceIM/Silence",
//			"https://github.com/rkalla/imgscalr",
//			"https://github.com/tianzhijiexian/CommonAdapter",
//			"https://github.com/RoaringBitmap/RoaringBitmap",
//			"https://github.com/swaldman/c3p0",
//			"https://github.com/amaembo/streamex",
//			"https://github.com/msgpack/msgpack-java",
//			"https://github.com/spring-projects/spring-data-mongodb",
//			"https://github.com/javiersantos/MLManager",
//			"https://github.com/Netflix/EVCache",
//			"https://github.com/Netflix/EVCache",
			
			
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
			"https://github.com/shyiko/mysql-binlog-connector-java",
	};
}
