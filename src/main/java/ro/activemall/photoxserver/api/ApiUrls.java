package ro.activemall.photoxserver.api;

public class ApiUrls {
	/** Globals **/
	public static final String JSON = "application/json;charset=UTF-8";
	public static final String ERROR_PATH = "/error";
	
	public static final String PRIVAPI_V1 = 			"/private/API/v1/";
	/** UsersController **/
	public static final String ACTIVATEACCOUNT = 		"/public/API/v1/activateAccount/{userId}/{token}";
	public static final String LOGGEDINUSER_JS =		PRIVAPI_V1+"loggedinUser.js";
	public static final String GETUSERBYID = 			PRIVAPI_V1+"getUserById/{userId}";
	public static final String INVITEACCOUNT = 			PRIVAPI_V1+"inviteAccount";
	public static final String CREATEACCOUNT = 			PRIVAPI_V1+"createAccount";
	public static final String UPDATEACCOUNT = 			PRIVAPI_V1+"updateUser";
	public static final String UPDATEPROFILE = 			PRIVAPI_V1+"updateUserProfile";
	public static final String SETROLEFORUSER = 		PRIVAPI_V1+"setRoleForUser";
	public static final String DELETEACCOUNT = 			PRIVAPI_V1+"deleteUser";
	public static final String EMAILUNIQUE = 			PRIVAPI_V1+"emailCheck";
	public static final String LOGINUNIQUE = 			PRIVAPI_V1+"loginnameCheck";
	public static final String GETUSERSPAGED = 			PRIVAPI_V1+"getUsersPagedAndOrdered";
	public static final String GETALLUSERS = 			PRIVAPI_V1+"getAllUsers";
	public static final String SEARCHUSERS = 			PRIVAPI_V1+"searchUsers";
	public static final String SETLANGUAGEFORUSER = 	PRIVAPI_V1+"setLanguageForLoggedInUser";	
	public static final String SAVEUSERSPREFERENCES = 	PRIVAPI_V1+"saveUserPreference";
	public static final String GETREFEREELIST = 		PRIVAPI_V1+"getRefereeList";
	public static final String SETPARENTOFUSER = 		PRIVAPI_V1+"setParentOfUser/{childUserId}/{parentUserId}";
	/** UploadController **/
	public static final String FILEUPLOAD = 			PRIVAPI_V1+"fileUpload";
	public static final String RESOURCEUPLOAD = 		PRIVAPI_V1+"resourceUpload/{resId}";
	public static final String GETRESOURCEHEAD = 		"/resources/{fileName:.+}";
	public static final String GETRESOURCE = 			"/resources/{fileName:.+}";
	/** TemplatesController **/
	public static final String SAVETEMPLATE = 			PRIVAPI_V1+"saveTemplate";
	/** ResourcesController **/
	public static final String SAVERESOURCE = 			PRIVAPI_V1+"saveResources";
	public static final String SAVECHILDRESOURCE = 		PRIVAPI_V1+"saveChildResource/{attachToResourceId}";
	public static final String CREATEROOTRESOURCE = 	PRIVAPI_V1+"createRootResource";
	public static final String CLONERESOURCE = 			PRIVAPI_V1+"cloneResource/{resourceId}/{targetUserId}";
	public static final String TRANSFERRESOURCE = 		PRIVAPI_V1+"transferResource/{resourceId}/{newParentId}";
	public static final String GETRESWITHEXTRAFIELDSET= PRIVAPI_V1+"getResourcesWithExtraFieldSet";
	public static final String LISTRESOURCES = 			PRIVAPI_V1+"listResources";
	public static final String GETRESOURCEBYID = 		PRIVAPI_V1+"getResourceById/{resourceId}";
	public static final String DELETERESOURCE = 		PRIVAPI_V1+"deleteResource";
	public static final String SEARCHRESOURCE = 		PRIVAPI_V1+"searchResources";
	public static final String DECLARETAGTORESOURCE = 	PRIVAPI_V1+"declareTagToResource/{attachToResourceId}";
	public static final String DELETETAGFROMRESOURCE = 	PRIVAPI_V1+"deleteTagFromResource/{attachToResourceId}";
	/** LogsViewController **/
	public static final String GETLOGSFORUSER = 		PRIVAPI_V1+"getLogsForUser/{userId}";
	public static final String COUNTLOGS = 				PRIVAPI_V1+"countLogsForUsers";
	public static final String DELETELOGSFORUSER = 		PRIVAPI_V1+"deleteLogsForUser/{userId}";
	/** PlatformSettingsController **/
	public static final String SAVESETTING = 			PRIVAPI_V1+"createOrUpdateSetting";
	/** RawMaterialsController **/
	public static final String STORESELLINGPRICE = 		PRIVAPI_V1+"storeSellingPrice/{productId}/{materialId}";
	public static final String DELETERAWGROUP = 		PRIVAPI_V1+"deleteRawMaterialGroup";
	public static final String GETALLRAWGROUPS = 		PRIVAPI_V1+"getAllMaterialsGrouped";
	public static final String SAVERAWGROUP = 			PRIVAPI_V1+"createOrUpdateRawMaterial/{groupId}";
	public static final String TRANSFERRAW = 			PRIVAPI_V1+"transferRawMaterial/{materialId}/{toGroupId}";
	public static final String DELETERAW = 				PRIVAPI_V1+"deleteRawMaterial";
	public static final String SAVERAW = 				PRIVAPI_V1+"createOrUpdateRawMaterialGroup";
	public static final String STOREMINQUANTITY = 		PRIVAPI_V1+"storeMinQuantity/{productId}"; 
	/** ProductsController **/
	public static final String GETPRODUCTBYID = 		PRIVAPI_V1+"getProductById/{productId}";
	public static final String DELETEPRODUCT = 			PRIVAPI_V1+"deleteProduct";
	public static final String CLONEPRODUCT = 			PRIVAPI_V1+"cloneProduct/{productId}";
	public static final String REMOVEPARENTOFPRODUCT = 	PRIVAPI_V1+"removeParentOfProduct/{productId}";
	public static final String ADDPRODUCTCHILD = 		PRIVAPI_V1+"addChildToProduct/{productId}/{childProductId}";
	public static final String REMOVERAWGROUPFROMPROD = PRIVAPI_V1+"removeRawGroupFromProduct/{productId}/{theGroupId}";
	public static final String ADDRAWGROUPTOPRODUCT = 	PRIVAPI_V1+"addRawGroupToProduct/{productId}/{toGroupId}";
	public static final String SAVEPRODUCT = 			PRIVAPI_V1+"createOrUpdateProduct";
	public static final String GETUNLISTEDPRODUCTS = 	PRIVAPI_V1+"getAllUnlistedProducts/{exceptProductId}";
	public static final String GETPRODUCTSLIST = 		PRIVAPI_V1+"getAllProducts";
	public static final String GETPRODUCTS = 			PRIVAPI_V1+"getProducts";

}
