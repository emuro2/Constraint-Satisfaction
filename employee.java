

/*
 * author: Erik Muro
 * 
 * 
 */


public class employee {
	
	public meeting [] meetings;

	public employee() {

	}
	
	public employee(meeting [] m)
	{
			meetings = m;
	}

	//Checks if assignment is consistent with employees
	static boolean checkEmployeeAssignment( int meetingNumber, CSP csp) 
	{
	
		
		
		//Check each employee to see if the employee has that meeting to go to
		for(int i = 0; i < csp.emp.length; i++)
		{
			
			for(int j = 0; j < csp.emp[i].meetings.length; j ++)
			{
	
				//check if employee has meeting, if so then make sure the assignment is consistent
				if(csp.emp[i].meetings[j].MeetingNum == meetingNumber)
				{
					//if meeting is equal to meeting 
					
					csp.emp[i].meetings[j].assigned = true;
					csp.emp[i].meetings[j].Timeslot =  csp.Meetings[meetingNumber-1].Timeslot;
	
					
					
					//employee at i-th index has meeting, and we need to check the assignment is consistent
					boolean re = empConsistency(csp.emp[i], csp);
					
					if(re == false)
					{
						//reset the meeting to not assigned because it is not a valid assignment
						csp.emp[i].meetings[j].assigned = false;
						csp.emp[i].meetings[j].Timeslot =  0;
						return false;
					}
					//else assignment is consistent with THIS employee, check the rest of the employees
					
				}
				
				
				
				//if not just skip employee because this assignment does not effect it
				
				
			}
	
		}
		
	
		//have gone through all the employees and employee's meetings
		//no conflicts so return true
		
		return true;
		
	}//end of checking consistency with employees

	//called from checkEmployeeAssignment, check the travel distances from meetings
	private static boolean empConsistency(employee employee, CSP csp) 
	{
	
		
		
		for(int i = 0; i < employee.meetings.length; i++)
		{
			
			//check if meeting was assigned (if not then the assignment can be true)
			if(employee.meetings[i].assigned)
			{
				int timeslot = employee.meetings[i].Timeslot;
				int specMeeting = employee.meetings[i].MeetingNum-1;
	
				for(int j = 0; j < employee.meetings.length; j++)
				{
				
					if(employee.meetings[j].assigned && (employee.meetings[j].MeetingNum != specMeeting+1))
					{
						int toMeeting = employee.meetings[j].MeetingNum-1;
						
						int TimeforThisMeeting = employee.meetings[j].Timeslot;
						
						if(timeslot == TimeforThisMeeting)
						{
						
							//employee cannot be at different meetings at the same time
							return false;
						}
							
						else if(timeslot > TimeforThisMeeting)
						{
							//meeting at j is before meeting at i
							int travelDist = csp.travelTime[toMeeting][specMeeting];
							
							if(travelDist >= Math.abs(timeslot - TimeforThisMeeting) )
							{
								return false;
							}
							
						}
						
						else if(timeslot < TimeforThisMeeting)
						{
							//meeting at i is before meeting at j
							int travelDist = csp.travelTime[specMeeting][toMeeting];
							
							if(travelDist >= Math.abs(timeslot - TimeforThisMeeting) )
							{
								return false;
							}
							
						}
					}
				}					
			}
					
		}
		
		//no conflicts!!! return true
		
		return true;
	}//end of employee consistency check

}
