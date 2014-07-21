package rp3.pos.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rp3.data.entity.EntityBase;
import rp3.db.sqlite.DataBase;
import rp3.pos.db.Contract;
import rp3.util.CursorUtils;
import rp3.util.Format;
import android.database.Cursor;

public class Transaction extends EntityBase<Transaction> {

	public static final int STATE_DEFAULT = 0;
	public static final int STATE_VALID = 1;
	
	private String transactionCode;
	private Date transactionDate;
	private int transactionNumber;
	private int transactionTypeId;
	private long id;
	private Integer clientId;
	private double total;
	private double discount;
	private double taxes;
	private double subtotal;
	private double quantity;
	private double subtotalBeforeTax;
	private String clientFullName;
	private String clientCardId;	
	private String detailResume;
	private String transactionTypeName;
	private int state;
	
	private TransactionType transactionType;
	private List<TransactionDetail> transactionDetails;
	private Cursor transactionDetailCursor;		
		
	private Client client;
	
	@Override
	public long getID() {		
		return id;
	}

	@Override
	public void setID(long id) {		
		this.id = id;
	}

	@Override
	public boolean isAutoGeneratedId() {		
		return true;
	}

	@Override
	public String getTableName() {		
		return Contract.Transaction.TABLE_NAME;
	}

	@Override
	public void setValues() {		
		setValue(Contract.Transaction.COLUMN_CLIENTID, this.clientId);
		setValue(Contract.Transaction.COLUMN_DISCOUNT, this.discount);
		setValue(Contract.Transaction.COLUMN_QUANTITY, this.quantity);
		setValue(Contract.Transaction.COLUMN_SUBTOTAL, this.subtotal);
		setValue(Contract.Transaction.COLUMN_SUBTOTALBEFORETAXES, this.subtotalBeforeTax);
		setValue(Contract.Transaction.COLUMN_TAXES, this.taxes);
		setValue(Contract.Transaction.COLUMN_TOTAL, this.total);
		setValue(Contract.Transaction.COLUMN_TRANSACTIONDATE, this.transactionDate);
		setValue(Contract.Transaction.COLUMN_TRANSACTIONNUMBER, this.transactionNumber);
		setValue(Contract.Transaction.COLUMN_TRANSACTIONTYPEID, this.transactionTypeId);
		setValue(Contract.Transaction.COLUMN_STATE, this.state);			
	}

	@Override
	public Object getValue(String key) {		
		return null;
	}

	@Override
	public String getDescription() {		
		return this.transactionCode;
	}
	
	
	public boolean isEmpty(){
		return this.getTransactionDetails().size() == 0 && (clientId == null || clientId == 0);
	}
	
	public int getTransactionTypeId() {
		return transactionTypeId;
	}
	public void setTransactionTypeId(int transactionTypeId) {
		this.transactionTypeId = transactionTypeId;
	}
	public String getTransactionCode() {
		return transactionCode;
	}
	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date date) {
		this.transactionDate = date;
	}	
	public int getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getClientFullName() {
		return clientFullName;
	}
	public void setClientFullName(String clientFullName) {
		this.clientFullName = clientFullName;
	}
	public String getClientCardId() {
		return clientCardId;
	}
	public void setClientCardId(String clientCardId) {
		this.clientCardId = clientCardId;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getTaxes() {
		return taxes;
	}
	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}	
	public double getQuantity() {
		return quantity;
	}
	public void setSubtotalBeforeTax(double subtotalBeforeTax) {
		this.subtotalBeforeTax = subtotalBeforeTax;
	}	
	public double getSubtotalBeforeTax() {
		return subtotalBeforeTax;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDetailResume() {
		return detailResume;
	}
	public void setDetailResume(String detailResume) {
		this.detailResume = detailResume;
	}	
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
		if(transactionType!=null){
			setTransactionTypeId(transactionType.getTransactionTypeId());
			setTransactionTypeName(transactionType.getName());
		}
		else{
			setTransactionTypeId(0);
			setTransactionTypeName(null);
		}
	}
	public String getTransactionTypeName() {
		return transactionTypeName;
	}
	public void setTransactionTypeName(String transactionTypeName) {
		this.transactionTypeName = transactionTypeName;
	}	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
		if(client!=null){
			setClientId(client.getId());
			setClientFullName(client.getFullName());
			setClientCardId(client.getCardId());
		}
		else{
			setClientId(null);
			setClientFullName(null);
			setClientCardId(null);
		}
	}
	public List<TransactionDetail> getTransactionDetails() {
		if(transactionDetails == null) transactionDetails = new ArrayList<TransactionDetail>();
		return transactionDetails;
	}
	public void setTransactionDetails(List<TransactionDetail> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
	public Cursor getTransactionDetailCursor() {
		return transactionDetailCursor;
	}
	public void setTransactionDetailCursor(Cursor transactionDetailCursor) {
		this.transactionDetailCursor = transactionDetailCursor;
	}	
	
	public void addDetail(TransactionDetail newDetail){
		TransactionDetail old = findDetailByProductId(newDetail.getProductId());
		if(old!=null){
			old.setQuantity( old.getQuantity() + newDetail.getQuantity());
			old.calculate();
		}
		else
		{
			getTransactionDetails().add(newDetail);
			newDetail.calculate();
		}
	}
	
	private TransactionDetail findDetailByProductId(long l){
		TransactionDetail result = null;
		for(TransactionDetail detail : transactionDetails){
			if(detail.getProductId() == l) {
				result = detail;
				break;
			}
		}
		return result;
	}
	
	public void calculate(){
		quantity = 0;
		discount = 0;
		subtotal = 0;
		subtotalBeforeTax = 0;
		
		StringBuilder resume = new StringBuilder();
		for(TransactionDetail detail : transactionDetails){
			quantity += detail.getQuantity();
			subtotal += detail.getSubtotal();
			discount += detail.getDiscount();
			subtotalBeforeTax += detail.getSubtotalBeforeTax();
			total += detail.getTotal();
			
			resume.append(Format.getDefaultNumberFormat(detail.getQuantity()) + " " + detail.getProduct().getDescription() + ", ");
		}
		if(resume.length()>0)
			setDetailResume( resume.substring(0,resume.length()-2) );		
		else
			setDetailResume( null );
		
		taxes = subtotalBeforeTax * 0.12;
		total = subtotalBeforeTax + taxes;
	}
	
	public static Cursor getTransactionSearchCursor(DataBase db, String termSearch)
	{		
		String query = Contract.Transaction.QUERY_TRANSACTION_SEARCH;
		
		Cursor c = db.rawQuery(query, "*" + termSearch + "*" );		
		return c;
	}
	
	public static Cursor getTransactionCursor(DataBase db, int transactionType)
	{		
		Cursor c = null;
		String query = null;
		if(transactionType != 0){			
			query = Contract.Transaction.QUERY_TRANSACTION_BY_TYPE;
			c = db.rawQuery(query, transactionType );
		}else
		{
			query = Contract.Transaction.QUERY_TRANSACTION;
			c = db.rawQuery(query);
		}
		return c;
	}
	
	public static Transaction getTransaction(DataBase db, long transactionId, boolean includeDetail)
	{		
		String query = Contract.Transaction.QUERY_TRANSACTION_BY_ID;
		 
		Cursor c = db.rawQuery(query, transactionId );
		Transaction transaction = null;
		if(c.moveToFirst())
		{
			transaction =  new Transaction();
			transaction.setTransactionCode( CursorUtils.getString(c,Contract.Transaction.FIELD_TRANSACTION_CODE) );
			transaction.setClientFullName( CursorUtils.getString(c,Contract.Transaction.FIELD_CLIENT_NAMES) );
			transaction.setClientCardId( CursorUtils.getString(c,Contract.Transaction.FIELD_CLIENT_CARDID) );
			transaction.setTotal( CursorUtils.getDouble(c,Contract.Transaction.FIELD_TOTAL) );
			transaction.setTransactionDate( CursorUtils.getDate(c,Contract.Transaction.FIELD_TRANSACTIONDATE) );
			transaction.setQuantity( CursorUtils.getDouble(c,Contract.Transaction.FIELD_QUANTITY) );
			transaction.setTaxes(CursorUtils.getDouble(c,Contract.Transaction.FIELD_TAXES) );
			transaction.setDiscount(CursorUtils.getDouble(c,Contract.Transaction.FIELD_DISCOUNT) );
			transaction.setSubtotal(CursorUtils.getDouble(c,Contract.Transaction.FIELD_SUBTOTAL) );
			transaction.setSubtotalBeforeTax(CursorUtils.getDouble(c,Contract.Transaction.FIELD_SUBTOTALBEFORETAXES) );
			
			transaction.setID( CursorUtils.getLong(c,Contract.Transaction._ID) );
			transaction.setTransactionTypeId( CursorUtils.getInt(c,Contract.Transaction.FIELD_TRANSACTIONTYPEID) );
			
			transaction.setDetailResume( CursorUtils.getString(c,Contract.Transaction.FIELD_DETAIL_RESUME) );
			
			TransactionType type = new TransactionType();
			type.setTransactionTypeId(transaction.getTransactionTypeId());
			type.setName( CursorUtils.getString(c,Contract.Transaction.FIELD_TRANSACTIONTYPE_DESCRIPTION) );
			
			transaction.setTransactionType(type);
			
			if(includeDetail){
				List<TransactionDetail> detail = TransactionDetail.getTransactionDetails(db, transactionId);
				transaction.setTransactionDetails(detail);
			}
		}
		return transaction;
	}
	
	public static int getNextTransactionNumber(DataBase db,int transactionTypeId){
		int value = 0;
		value = db.queryMaxInt(Contract.Transaction.TABLE_NAME, 
				Contract.Transaction.COLUMN_TRANSACTIONNUMBER,
				Contract.Transaction.COLUMN_TRANSACTIONTYPEID + " = ?",
				transactionTypeId);
		
		return value;
	}
	
	public static boolean insertOrUpdate(DataBase db, Transaction t){		
		if(t.getID() == 0){
			if(!t.isEmpty())
				return insert(db, t);
			else
				return true;
		}
		else
			return update(db, t);
	}
	
	@Override
	protected void onPrepareInsert(DataBase db, int actionId) {		
		super.onPrepareInsert(db, actionId);
		this.setTransactionNumber(getNextTransactionNumber(db, this.getTransactionTypeId()));				
	}
	
	@Override
	protected boolean insertDb(DataBase db) {
		boolean result = false;
		try
		{
			db.beginTransaction();
			result = super.insertDb(db);
			
			if(result){
				TransactionExt ext = new TransactionExt();				
				result = TransactionExt.insert(db, ext);
			}
			
			if(result){
				for(TransactionDetail d : this.getTransactionDetails()){
					d.setTransactionId(this.id);
					result = TransactionDetail.insert(db, d);					 
					if(!result) break;					
				}								
				
				if(result)
					db.commitTransaction();
			}					
						
		}catch(Exception ex){
			result = false;
			db.rollBackTransaction();
		}finally{
			db.endTransaction();			
		}
		return result;
	}
	
	@Override
	protected boolean updateDb(DataBase db) {		
		boolean result = false;
		try
		{
			db.beginTransaction();
			result = super.updateDb(db);
			
			if(result){
				for(TransactionDetail d : this.getTransactionDetails()){
					d.setTransactionId(this.id);
					if(d.getID() == 0)
						result = TransactionDetail.insert(db, d);
					else
						result = TransactionDetail.update(db, d);
					if(!result) break;					
				}								
				
				if(result)
					db.commitTransaction();								
			}					
						
		}catch(Exception ex){
			result = false;
			db.rollBackTransaction();
		}finally{
			db.endTransaction();			
		}
		return result;
	}		
	
	@Override
	protected boolean deleteDb(DataBase db) {
		if(this.getID() == 0) return true;
		
		db.beginTransaction();
		boolean result = false;
		try
		{
			result = TransactionDetail.deleteAllFromTransaction(db, this.getID());
			if(result){
				result = super.deleteDb(db);
				if(result)
					db.commitTransaction();
			}									
			
		}catch(Exception ex){
			result = false;
			db.rollBackTransaction();
		}
		finally{
			db.endTransaction();
		}
		return result;
	}	
	
	class TransactionExt extends EntityBase<TransactionExt> {		
		
		@Override
		public long getID() {			
			return id;
		}

		@Override
		public void setID(long idext) {
			id = idext;
		}

		@Override
		public boolean isAutoGeneratedId() {
			return false;
		}

		@Override
		public String getTableName() {			
			return Contract.TransactionExt.TABLE_NAME;
		}

		@Override
		public void setValues() {				
			setValue(Contract.TransactionExt.COLUMN_CLIENT_CARID, clientCardId);
			setValue(Contract.TransactionExt.COLUMN_CLIENT_NAMES, clientFullName);
			setValue(Contract.TransactionExt.COLUMN_CODE, transactionCode);
			setValue(Contract.TransactionExt.COLUMN_DETAIL_RESUME, detailResume);
			setValue(Contract.TransactionExt.COLUMN_TRANSACTIONTYPE_NAME, transactionTypeName);
		}

		@Override
		public Object getValue(String key) {
			return null;
		}

		@Override
		public String getDescription() {			
			return transactionCode;
		}
		
	}
	
}
