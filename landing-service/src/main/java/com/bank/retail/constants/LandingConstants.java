package com.bank.retail.constants;

public interface LandingConstants {

	public static final String RESULT_SET = "ResultSet";

	// Response field constants
	public static final String STATUS = "Status";
	public static final String MESSAGE = "Message";
	public static final String PROCESS_ID = "ProcessID";
	public static final String ERROR_FLAG = "ErrorFlag";

	// Adapter response structure constants
	public static final String GET_ACCOUNT_DETAILS_RESPONSE = "GetAccountDetailsResponse";
	public static final String ACCOUNT_NAME = "AccountName";
	public static final String ACCOUNT_NUMBER = "AccountNumber";
	public static final String LEDGER_NAME = "LedgerName";
	public static final String CURRENCY_NAME = "CurrencyName";
	public static final String DATE_OPEN = "DateOpen";
	public static final String CURRENT_BAL = "CurrentBal";
	public static final String CLEAR_BAL = "ClearBal";
	public static final String AVAILABLE_BAL = "AvailableBal";
	public static final String STATUS_SUCCESS="0";

	// Service identifier
	public static final String LANDING_SERVICE_NAME = "landing-service";
}