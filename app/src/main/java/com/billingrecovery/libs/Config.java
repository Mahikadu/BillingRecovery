package com.billingrecovery.libs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by balamurugan@adstringo.in on 23-12-2015.
 */
public class Config {

    public static final String dbName = "billingrecovery";  //Create db name in App42 api


    public static final String collectionAdmin = "Admin";
    public static final String collectionGenerateBill = "GenerateBill";

    public static final String collectionService = "service";
    /* public static final String collectionServiceHistory = "servicehistory";
     public static final String collectionServiceCustomer = "servicecustomer";
     public static final String collectionServices = "service";*/
    public static final String collectionProvider = "provider";
    public static final String collectionProviderDependent = "providerdependent";
    public static final String collectionCustomer = "customer";
    public static final String collectionActivity = "activity";
    public static final String collectionDependent = "dependent";
    public static final String collectionNotification = "notification";
    public static final String collectionLoginLog = "login_log";
    public static final String collectionCheckInCare = "checkincare";
    public static final String collectionUpdateVersion = "updateversion";

   // public static final int iSdkVersion = Build.VERSION.SDK_INT;
   // public static final int iAppVersion = BuildConfig.VERSION_CODE;

    public static final String strOs = "android";
    public static final String strAppId = "910502819263"; //Google Devloper console (Project Number)
    //sample for tessting
    //public static final String strAppId = "425694689640";

    public static final int START_CAMERA_REQUEST_CODE = 1;
    public static final int START_GALLERY_REQUEST_CODE = 2;
    //public static final int CACHE_EXPIRE = 1;//In Minutes

    public static final String SERVICE_MESSAGE = "com.hdfc.services.SyncService.INTENT_MESSAGE";
    public static final String SERVICE_RESULT = "com.hdfc.services.SyncService.INTENT_RESULT";
    public static final String SERVICE_RESULT_VALUE = "message";

    public static final int intWidth = 300, intHeight = 300;
    //todo image compression parameters
    public static final int intCompressWidth = 500, intCompressHeight = 500, iQuality = 60;
    /* public static final String[] weekNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};*/
    /* public static final String[] months = {"January", "February", "March", "April", "May", "June",
             "July", "August", "September", "October", "November", "December"};*/
    /*public static final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};*/
    public static final boolean isDebuggable = true;
   // public static final String string = Utils.getStringJni();
    public static final boolean release = false;
    public static final boolean DEVELOPMENT = false;

    public final static String strCustomerImageName = "provider_image";
    public static int intClientScreen = 2;
    public static int intRatingsScreen = 4;
    public static int intNotificationScreen = 3;
    public static int intDashboardScreen = 1;
    public static int intScreenWidth = 0;
    public static int intScreenHeight = 0;

    //Login specific //User Specific clear at logout or whenever needed

    //public static ArrayList<FileModel> fileModels = new ArrayList<>();

   // public static ArrayList<ActivityModel> activityModels = new ArrayList<>();
    public static ArrayList<String> dependentIds = new ArrayList<>();
    public static ArrayList<String> customerIds = new ArrayList<>();
    public static ArrayList<String> customerIdsCopy = new ArrayList<>();

    public static ArrayList<String> strActivityIds = new ArrayList<>();

    public static ArrayList<String> dependentIdsAdded = new ArrayList<>();
    public static ArrayList<String> customerIdsAdded = new ArrayList<>();
//    public static ArrayList<DependentModel> dependentModels = new ArrayList<>();
//    public static ArrayList<CustomerModel> customerModels = new ArrayList<>();
//    public static ProviderModel providerModel = null;
//    public static CustomerModel customerModel = null;
//    public static DependentModel dependentModel = null;
//    public static CheckInCareModel checkInCareModel = null;
//    public static UpdateVersionModel updateversionModel = null;
    public static JSONObject jsonObject = null;
    public static int intSelectedMenu = 0;

   // public static ArrayList<ClientModel> clientModels = new ArrayList<>();
    //public static ArrayList<UpdateVersionModel> updateVersionModel = new ArrayList<>();
   // public static ArrayList<NotificationModel> notificationModels = new ArrayList<>();

   // public static ArrayList<CheckInCareModel> checkInCareModels = new ArrayList<>();
   // public static ArrayList<PictureModel> roomtypeName = new ArrayList<>();

   // public static ArrayList<ClientNameModel> clientNameModels = new ArrayList<>();
    public static Map<String, List<String>> serviceNameModels = new HashMap<>();
    public static double iRatings = 0;
    public static int iRatingCount = 0;
    //public static List<FeedBackModel> feedBackModels = new ArrayList<>();

    public static ArrayList<String> strServcieIds = new ArrayList<>();
   // public static ArrayList<ServiceModel> serviceModels = new ArrayList<>();
    public static ArrayList<String> servicelist = new ArrayList<>();
    public static ArrayList<String> serviceCategorylist = new ArrayList<>();

    public static ArrayList<String> strDependentNames = new ArrayList<>();
    public static ArrayList<String> strCustomerNames = new ArrayList<>();

    public enum ActivityStatus {NEW, OPEN, INPROCESS, COMPLETED}

    public enum MilestoneStatus {INACTIVE, OPENED, INPROCESS, COMPLETED, REOPENED, PENDING}
}