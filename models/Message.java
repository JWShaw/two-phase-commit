package models;

public enum Message 
{
	PREPARE,	// TM -> RM messages
	COMMIT,		
	ABORT,
	
	PREPARED,	// RM -> RM messages
	COMMITTED,
	ABORTED;
}
