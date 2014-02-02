
/*
 * @author Erik Muro
 * emuro2
 * 
 * 
 * CSP
 * 
 * TimeSlots = Number of TimeSlots with the specified problem
 * numMeetings = Number of Meetings with the specified problem
 * Meetings = Array of meeting variables (more in meeting.java) 
 * emp = Array of employees (more in employee.java)
 * travelTime = Double Array of integers, the travel distances from a meeting to another
 * MostConstrained = Queue that self maintains my Most Constrained Variables
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
	
	
	
	
	
	//CSP sample problem to check on a small test case first
	public static void CSPSample()
	{
		
		/*
		  
		  numMeetings = number of meetings 
		  
		  TimeSlots = the number of time slots available
		 
		  emp = array of the number of employees 
		 
		  each employee has an array of meetings which hold meeting number, 
		  if assigned yet, and time slot it is assigned too 
		
		  Meetings = array of meetings basically to simplify the printing of the final result
		  
		  travel = double array of the time it takes to travel from one meeting to another
	
		*/
		

		int numMeetings = 5;
		
		int TimeSlots = 5;

		
		//initializing Meetings array
		meeting [] Meetings = new meeting[numMeetings];
		
		for(int i = 0; i < Meetings.length; i++)
		{
			Meetings[i]= new meeting();
			Meetings[i].MeetingNum = i+1;
		}
		
		int [][] travel = new int[5][5];
		
		
		
		
		//initializing Employee array
		employee [] emp; 		
		emp = new employee[6];
		
		for(int i = 0; i < emp.length; i++)
		{
					//System.out.println(i);
			emp[i]= new employee();
			if(i == 3)
				emp[i].meetings = new meeting[3];
			else
				emp[i].meetings = new meeting[2];
			
			for(int j = 0; j< emp[i].meetings.length; j++ )
			{
				emp[i].meetings[j] = new meeting();
			}
		}
		
		
		
		
		
		//input numbers from txt file
		try {

			//grab txt file for CSP problem
			File file = new File("SampleProblem.txt");
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

			//set up travel distances
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


		
		//initialize the CSP
		CSP CSPSample = new CSP( TimeSlots, numMeetings, Meetings, emp, travel);
		
		
		
		System.out.println("Number of meetings: " + CSPSample.numMeetings);
		System.out.println("Number of employees: " + CSPSample.emp.length);
		System.out.println("Number of time slots: " + CSPSample.TimeSlots);
		System.out.println();
		
	
		System.out.println("Meetings each employee must attend: ");

		
		//print out the meetings each employee has to attend
		for(int i = 0; i < CSPSample.emp.length; i++)
		{
			System.out.print(i+1 + ": ");
			for(int j = 0; j < CSPSample.emp[i].meetings.length; j ++)
			{
				System.out.print(CSPSample.emp[i].meetings[j].MeetingNum + " " );//+ "Meeting TimeSlot: "+ emp[i].meetings[j].Timeslot + " / " );
				
			}

			System.out.println();
		}
		
		System.out.println();
		


		
		System.out.println("Travel Time between meetings:");
		
		for(int i = 0; i < CSPSample.travelTime.length; i++)
		{
			System.out.print(i+1 + ": ");
			for(int j = 0; j < CSPSample.travelTime.length; j++)
			{
				System.out.print(CSPSample.travelTime[i][j] + " " );
				
			}
			System.out.println();
			
		}
		
		System.out.println();
		
		
		
		//BackTracking!!!
		
		
		boolean result = CSP.BackTracking( -1 ,  CSPSample);
		
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
			System.out.println("Satisfied");
		}
		
	}

	

	//BackTracking function (Brute Force)
	static boolean BackTracking( int meetingNumber ,  CSP csp) 
	{
		
		//if assignment is complete then return assignment
		/*meeting number and timeslot it is assigned too */
		if( assignComplete( meetingNumber,  csp) )
		{
			return true;
		}
		
		
		
		//get a meeting to assign next
		int j = meeting.getMeeting(csp);
		
		
			//select a time slot for the meeting
			for(int i = 0; i < csp.TimeSlots; i++)
			{
				
			
				//for each value in the Domain, check if the assignment is consistent with the Constraints
				//if so, add to the variable and recurse with that new assignment
				
				csp.Meetings[j].Timeslot = i+1;
				csp.Meetings[j].assigned = true;
			
				int meetNum = j+1;
				
				//check consistency with employees
				if( employee.checkEmployeeAssignment(meetNum, csp) || j ==0)
				{
			
					//just count an attempted variable assignment when we actually find a consistent value.
					csp.GlobalVarAssign = csp.GlobalVarAssign+1;
										
					boolean recurse = BackTracking( meetNum,  csp); 
	
					//once returned from recurse...if the result is failure... then remove assignment and continue searching
					if(recurse == true)
					{
						
						return true;
					}

				}
				//else assignment is bad and switch the time slot for meeting, go on to the next one
				//un-assign that assignment
				unassign(j, csp);
		
			}

		//return failure
		return false;

	}//end of Backtracking

	
	



//Checks the Meetings variable in the CSP to see if the assignment is complete
	/*
	 * @param meetingNumber is the number of the meeting which was assigned (not 0 based index)
	 * 
	 * @param csp is the CSP to satisfy 
	 * 
	 */
	private static boolean assignComplete( int meetNumber, CSP csp) 
	{
	
		//-1 is to initialize Backtracking function
		if(meetNumber == -1)
		{	
			return false;
		}
		
		
		
		//check if the assignment doesn't violate any constraints
		//check each employee to see that they can go to each meeting
		boolean employ = employee.checkEmployeeAssignment(meetNumber , csp);
		
		
		if(employ == false)
		{
			return false;
		}
		
		
		//check that each meeting has an assignment 
		for(int i = 0; i < csp.Meetings.length; i++)
		{
			
			if(!csp.Meetings[i].assigned)	
			{
				return false;
			}

		}
		
		return true;
	}//end of checking if the assignment is Complete



	
	
	
	
	
	
	
	
	

	//un-assign all the meetings associated with j and after j in the meeting array
	private static void unassign(int j, CSP csp) 
	{
		
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
	
	
	
	

}//end CSP class
