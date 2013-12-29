
/*
 * @author Erik Muro
 * 
 * 
 * 
 * Constraint Satisfaction Problem: Initializes the CSP and calls the backtracking function to complete assignment
 * 
 * TimeSlots = Number of TimeSlots with the specified problem
 * numMeetings = Number of Meetings with the specified problem
 * Meetings = Array of meeting variables (more in meeting.java) 
 * emp = Array of employees (more in employee.java)
 * travelTime = Double Array of integers, the travel distances from a meeting to another
 * MostConstrained = Queue that self maintains my Most Constrained Variables
 * 
 *
 *
 */




import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;



public class CSP {
	

	public int TimeSlots;
	public int numMeetings;
	public meeting [] Meetings;
	public employee [] emp; 
	public int [][] travelTime;
	public Queue MostConstrained;
	
	// Number of Variable Assigned Counters
	public int GlobalIndex, GlobalVarAssign;
	
	
	
	//CSP constructor
	public CSP( int ts, int nm, meeting [] M, employee [] ep, int [][] travel) 
	{
		
		travelTime = travel;
		
		TimeSlots = ts;
		
		numMeetings = nm;
		
		Meetings = M;
		
		emp =ep;

	}//end of constructor
	
	
	
	
	
	
	
	
	
	// CSP problem 1
	public static void CSProblem1() 
	{
		
		System.out.println("Problem 1");
		
		
		
		/*
		  
		  numMeetings = number of meetings 
		  
		  TimeSlots = the number of time slots available
		 
		  emp = array of the number of employees 
		 
		  each employee has an array of meetings which hold meeting number, 
		  if assigned yet, and time slot it is assigned too 
		
		  Meetings = array of meetings basically to simplify the printing of the final result
		  
		  travel = double array of the time it takes to travel from one meeting to another
	
		*/
		

		int numMeetings = 20;
		
		int TimeSlots = 12;
		
		
		//initialize
		meeting [] Meetings = new meeting[numMeetings];
		
		for(int i = 0; i < Meetings.length; i++)
		{
			Meetings[i]= new meeting();
			Meetings[i].MeetingNum = i+1;
		}
		
		int [][] travel = new int[20][20];
	
		employee [] emp; 		
		emp = new employee[33];
		
		for(int i = 0; i < emp.length; i++)
		{
					//System.out.println(i);
			emp[i]= new employee();
			if(i ==9 || i==12 || i==14)
				emp[i].meetings = new meeting[6];
			else
				emp[i].meetings = new meeting[5];
			
			for(int j = 0; j< emp[i].meetings.length; j++ )
			{
				emp[i].meetings[j] = new meeting();
			}
		}
		
	
		
		//input numbers from txt file
		try {

			//input the CSP problem 1
			File file = new File("problem1.txt");
			Scanner s = new Scanner(file);
			
			
			//set up employees
			for(int k = 0; k < emp.length; k++)
			{
				for(int l =0; l < emp[k].meetings.length; l++)
				{
					if(s.hasNextInt())
						emp[k].meetings[l].MeetingNum = s.nextInt();
				}
			}

			for(int i = 0; i < travel.length; i++)
			{
				for(int j = 0; j < travel.length; j++)
				{
					if(s.hasNextInt())
						travel[i][j]= s.nextInt();
				}
			}

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		
		
		CSP CSPSample = new CSP( TimeSlots, numMeetings, Meetings, emp, travel);
		CSPSample.MostConstrained = new Queue();
		CSPSample.GlobalIndex = 0;
		CSPSample.GlobalVarAssign = 0;
		
	
		
		//BackTracking!!!
		
		//pass in -1 to initially start the backtracking function
		boolean result = CSP.BackTracking( -1 , CSPSample);
		
		if(result == false)
		{
			System.out.println();
			System.out.println("Could not Satisfy the Constraints!");
		}
	
		else
		{
			//print out the time slots and meetings at the time slots
			for(int i = 0; i< CSPSample.Meetings.length; i++)
			{
				System.out.println("Meeting "+(i+1)+" is scheduled at time " +  CSPSample.Meetings[i].Timeslot);
			}
			
			System.out.println("Number of Variables assigned: " + CSPSample.GlobalVarAssign);
			
			System.out.println("Satisfied");
		}
	}//end of problem 1
	
	





	//BackTracking function (Brute Force)
	/*
	* Iterates through each assignment and checks if no constraints are violated
	*
	* @param int meetingNumber is the meetingNumber assigned before the call to recurse
	* @param CSP csp is the constraint satisfaction problem with all variables and assignments
	*/
	static boolean BackTracking( int meetingNumber ,  CSP csp) 
	{
		//complete?
		if( assignComplete( meetingNumber,  csp) )
		{
			return true;
		}
		
	
		int j = getMeeting(csp);
		
	
			for(int i = 0; i < csp.TimeSlots; i++)
			{
				
					csp.Meetings[j].Timeslot = i+1;
					csp.Meetings[j].assigned = true;
				
					int meetNum = j+1;
					
					if( checkEmployeeAssignment(meetNum, csp) || j ==0)
					{
			
						csp.GlobalVarAssign++;
						
						boolean recurse = BackTracking( meetNum,  csp); 

						if(recurse == true)
						{
							return true;
						}
						
					}
					//else assignment is bad 
					unassign(j, csp);
			
			}

		return false;

	}//end of Backtracking







//un-assign all the meetings associated with j and after j in the meeting array
private static void unassign(int j, CSP csp) {
	
		for(int i = j; i < csp.Meetings.length; i++ )
		{
			csp.Meetings[i].Timeslot = 0;
			csp.Meetings[i].assigned = false;
			
			for(int in = 0; in < csp.emp.length; in++)
			{
				
				for(int jl = 0; jl < csp.emp[in].meetings.length; jl ++)
				{

					//check if employee has meeting, if so then make sure the assignment is consistent
					if(csp.emp[in].meetings[jl].MeetingNum == i+1)
					{
						csp.emp[in].meetings[jl].assigned = false;
						csp.emp[in].meetings[jl].Timeslot = 0;
					}
				}
			}
		}
		
	}







  //return a meeting at has not been assigned yet
  private static int getMeeting(CSP csp) 
  {
  	for(int i = 0; i < csp.Meetings.length; i++)
  	{
  		if(!csp.Meetings[i].assigned)
  		{
  			return i;
  		}
  	}

    return 0;
  }
	
	
	



	//Checks the Meetings variable in the CSP to see if the assignment is complete
	/*
	 * @param meetingNumber is the number of the meeting which was assigned (not 0 based index)
	 * 
	 * @param csp is the CSP to satisfy 
	 * 
	 */
	private static boolean assignComplete( int meetNumber, CSP csp) 
	{
		if(meetNumber == -1)
		{
			return false;
		}
		
		//check if the assignment doesn't violate any constraints
		boolean employ = checkEmployeeAssignment(meetNumber , csp);
		
		if(employ == false)
		{
			return false;
		}
		
		for(int i = 0; i < csp.Meetings.length; i++)
		{
			if(!csp.Meetings[i].assigned)	
			{
				return false;
			}
		}
		
		return true;
	}//end of checking if the assignment is Complete



	
	
	
	
	
	
	
	
	

	//Checks if assignment is consistent with employees
	private static boolean checkEmployeeAssignment( int meetingNumber, CSP csp) 
	{
		for(int i = 0; i < csp.emp.length; i++)
		{
			
			for(int j = 0; j < csp.emp[i].meetings.length; j ++)
			{
				//check if employee has meeting, if so then make sure the assignment is consistent
				if(csp.emp[i].meetings[j].MeetingNum == meetingNumber)
				{
					csp.emp[i].meetings[j].assigned = true;
					csp.emp[i].meetings[j].Timeslot =  csp.Meetings[meetingNumber-1].Timeslot;

					
					
					//employee at i-th index has meeting, and we need to check that the assignment is consistent
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
			}
		}
		

		//have gone through all the employees and employee's meetings
		//no conflicts so return true
		
		return true;
	
	}//end of checking consistency with employees


	
	
	
	
	
	
	/*
	* called from checkEmployeeAssignment, checks the travel distances from meetings
	*/
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
	
	
	
	

}//end CSP class
