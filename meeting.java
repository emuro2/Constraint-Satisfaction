
/*
 * author: Erik Muro
 * 
 * 
 */



public class meeting {

	public int MeetingNum;
	public boolean assigned;
	public int Timeslot;
	public int count;
	
	public meeting() {
		// TODO Auto-generated constructor stub
	}
	
	public meeting(int n, boolean assign)
	{
		MeetingNum = n;
		assigned = assign;
		
		//initially time slot is null
		Timeslot = -1;
	}

	
	public void addTimeSlot(int t)
	{
		Timeslot = t;
		
	}

	//return a meeting at has not been assigned yet
	static int getMeeting(CSP csp) {
		
		
		for(int i = 0; i < csp.Meetings.length; i++)
		{
			if(!csp.Meetings[i].assigned)
			{
				return i;
			}
		}
	// TODO Auto-generated method stub
	return 0;
	}
}
