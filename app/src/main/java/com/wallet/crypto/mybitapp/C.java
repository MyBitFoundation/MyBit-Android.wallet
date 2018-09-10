package com.wallet.crypto.mybitapp;

public abstract class C {

    public static final int IMPORT_REQUEST_CODE = 1001;
    public static final int EXPORT_REQUEST_CODE = 1002;
    public static final int SHARE_REQUEST_CODE = 1003;
    public static final int WALLET_CREATION_RESULT_CODE = 1004;
    public static final int WALLET_REQUEST_CODE = 1005;

    public static final int TOKEN_BALANCE_SCALE = 4;

    public static final String MYB_SYMBOL = "MYB";
    public static final String MYBIT_TOKEN_NAME = "MyBit";
    public static final String MYBIT_TOKEN_ADDRESS = "0x94298F1e0Ab2DFaD6eEFfB1426846a3c29D98090";
    public static final int MYBIT_DECIMALS = 8;

    public static final String ETH_SYMBOL = "ETH";
    public static final String ETHEREUM_ADDRESS = "0x0000000000000000000000000000000000000000";
    public static final String ETHEREUM_NETWORK_NAME = "Ethereum";
    public static final String ETHEREUM_RPC_SERVER_URL = "https://mainnet.infura.io/llyrtzQ3YhkdESt2Fzrk";
    public static final String ETHEREUM_BACKEND_URL = "https://api.trustwalletapp.com/";
    public static final String ETHERSCAN_URL = "https://etherscan.io/";
    public static final int ETHEREUM_CHAIN_ID = 1;
    public static final int ETHER_DECIMALS = 18;

    public static final String USD_SYMBOL = "$";

    public static final String GWEI_UNIT = "Gwei";

    public static final String WEI_IN_ETH = "1000000000000000000";

    public static final String EXTRA_ADDRESS = "ADDRESS";
    public static final String EXTRA_CONTRACT_ADDRESS = "CONTRACT_ADDRESS";
    public static final String EXTRA_DECIMALS = "DECIMALS";
    public static final String EXTRA_SYMBOL = "SYMBOL";
    public static final String EXTRA_BALANCE = "BALANCE";
    public static final String EXTRA_SENDING_TOKENS = "SENDING_TOKENS";
    public static final String EXTRA_TO_ADDRESS = "TO_ADDRESS";
    public static final String EXTRA_AMOUNT = "AMOUNT";
    public static final String EXTRA_GAS_PRICE = "GAS_PRICE";
    public static final String EXTRA_GAS_LIMIT = "GAS_LIMIT";
    public static final String EXTRA_CANCEL_BACK = "CANCEL_BACK";
    public static final String EXTRA_ONLY_CHECK_PASS = "ONLY_CHECK_PASS";
    public static final String EXTRA_REMOVE_PASS = "REMOVE_PASS";

    public static final String DEFAULT_GAS_PRICE = "21000000000";
    public static final String DEFAULT_GAS_LIMIT = "90000";
    public static final String DEFAULT_GAS_LIMIT_FOR_TOKENS = "144000";
    public static final long GAS_LIMIT_MIN = 21000L;
    public static final long GAS_LIMIT_MAX = 300000L;
    public static final long GAS_PRICE_MIN = 1000000000L;
    public static final long NETWORK_FEE_MAX = 20000000000000000L;

    public static final int TRANSACTIONS_PRECISION = 5;

    public interface ErrorCode {
        int UNKNOWN = 1;
        int WALLET = 2;
        int WALLET_ALREADY_EXIST = 3;
        int NO_SESSION = 4;
        int INSUFFICIENT_FUNDS = 5;
    }

    public interface Key {
        String WALLET = "wallet";
        String TRANSACTION = "transaction";
        String SHOULD_SHOW_SECURITY_WARNING = "should_show_security_warning";
    }
}
