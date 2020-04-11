package models;

public enum PState 
{
	WORKING,
	PREPARED,
	COMMITTED,
	ABORTED,
	
	INITIALIZING, // TM-only states
	PREPARING;
}
