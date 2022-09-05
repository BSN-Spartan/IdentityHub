package com.reddate.hub.sdk.constant;

/**
 * The request URL constant of the hub service in BSN.
 */
public class ServiceURL {

	/**
	 * URL of create hub service endpoint in BSN
	 */
	public static final String PUT_DID_ON_CHAIN = "/hub/putDoc";

	/**
	 * URL of verify hub document service endpoint in BSN
	 */
	public static final String VERIFY_DID_DOCUMENT = "/hub/verifyDoc";

	/**
	 * URL of query hub document service endpoint in BSN
	 */
	public static final String GET_DID_DOCUMENT = "/hub/getDoc";

	/**
	 * URL of reset hub document main authenticate service endpoint in BSN
	 */
	public static final String REST_DID_AUTH = "/hub/resetDidAuth";
	
	/**
	 * URL of verify hub identifier sign service endpoint in BSN
	 * 
	 */
	public static final String VERIFY_DID_SIGN = "/hub/verifyDIdSign";

	/**
	 * URL of register authenticate issuer service endpoint in BSN
	 */
	public static final String REGISTER_AUTH_ISSUER = "/hub/registerAuthIssuer";

	/**
	 * URL of query authenticate issuer service endpoint in BSN
	 */
	public static final String QUERY_AUTH_ISSUER_LIST = "/hub/queryAuthIssuerList";

	/**
	 * URL of register CPT template service endpoint in BSN
	 */
	public static final String REGISTER_CPT = "/hub/registerCpt";

	/**
	 * URL of query register CPT template list service endpoint in BSN
	 */
	public static final String QUERY_CPT_DID = "/hub/queryCptList";

	/**
	 * URL of query register CPT template detail information service endpoint in BSN
	 */
	public static final String QUERY_CPT_INFO = "/hub/queryCptById";

	/**
	 * URL of update register CPT template service endpoint in BSN
	 */
	public static final String UPDATE_CPT = "/hub/updateCpt";

	/**
	 * URL of create credential service endpoint in BSN
	 */
	public static final String CREATE_CREDENTIAL = "/hub/createCredential";

	/**
	 * URL of verify issued credential service endpoint in BSN
	 */
	public static final String VERIFY_CREDENTIAL = "/hub/verifyCredential";

	/**
	 * URL of revoke issued credential service endpoint in BSN
	 */
	public static final String REVOKED_CRED = "/hub/revokeCredential";

	/**
	 * URL of query revoke issued credential service endpoint in BSN
	 */
	public static final String GET_REVOKED_CRED_LIST = "/hub/getRevokedCredList";

	/**
	 * URL of register an identify HUB user service endpoint in BSN
	 */
	public static final String HUB_REGISTER = "/hub/regiter";
	
	public static final String HUB_QUEERY_TYPE = "/hub/getType";

	/**
	 * URL of register an identify HUB user by public key service endpoint in BSN
	 */
	public static final String HUB_REGISTER_BY_PUBLIOCKEY = "/hub/regiterByIdPublicKey";
	
	/**
	 * URL of save some resource to identify HUB service endpoint in BSN
	 */
	public static final String HUB_SAVE_RESOURCE = "/hub/saveResource";

	/**
	 * URL of query resource in identify HUB service endpoint in BSN
	 */
	public static final String HUB_QUERY_RESOURCE = "/hub/getResource";

	/**
	 * URL of query resource history in identify HUB service endpoint in BSN
	 */
	public static final String HUB_QUERY_RESOURCE_HISTORY = "/hub/getResourceHistory";

	/**
	 * URL of delete resource in identify HUB service endpoint in BSN
	 */
	public static final String HUB_DELETE_RESOURCE = "/hub/deleteResource";

	/**
	 * URL of create permission for saved resource(s) in identify HUB service
	 * endpoint in BSN
	 */
	public static final String HUB_CREATE_PERMISSION = "/hub/createPerm";

	/**
	 * URL of delete created permission for saved resource(s) in identify HUB
	 * service endpoint in BSN
	 */
	public static final String HUB_DELETE_PERMISSION = "/hub/deletePermission";

	/**
	 * URL of query created grant for other identify HUB user in identify HUB
	 * service endpoint in BSN
	 */
	public static final String HUB_QUERY_PERMISSION = "/hub/queryPermission";

	/**
	 * URL of query grant resource to yourself in identify HUB service endpoint in
	 * BSN
	 */
	public static final String HUB_QUERY_GRANT_PERMISSION = "/hub/queryGrantedPermission";

	/**
	 * URL of check grant that if one identify HUB user have the grant for the
	 * resource in identify HUB service endpoint in BSN
	 */
	public static final String HUB_CHECK_PERMISSION = "/hub/checkPermission";

	
	/**
	 * URL of query current block info in BSN
	 */
	public static final String GET_BLOCK_INFO = "/hub/getBlockInfo";
	
	/**
	 * URL of transfer owner in identify HUB service endpoint in BSN
	 */
	public static final String HUB_TRANSFER_OWNER = "/hub/transferOwner";
	
}
