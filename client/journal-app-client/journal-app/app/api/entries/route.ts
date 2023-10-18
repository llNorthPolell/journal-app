import {NextRequest, NextResponse} from 'next/server';
import { getServerSession } from 'next-auth';
import { authOptions } from '../auth/[...nextauth]/route';
import { JournalEntryForm } from '../../../models/journalEntry';


const submitJournalEntry = async (journalEntry: JournalEntryForm,journalId:string, id_token : string)=>{
    try {
        const url = `${process.env.NEXT_PUBLIC_JOURNAL_APP_API}/${journalId}/journalEntries`
    
        const res = await fetch(
          url,
          {
            method: "POST",
            headers: {
              authorization: `Bearer ${id_token}`,
              "Content-Type": "application/json"
            },
            body: JSON.stringify(journalEntry)
          }
        )
    
        return await res.json();
      }
      catch(error){
        console.log(error);
        return {};
      }
}


export async function handler(
  req: NextRequest,
) {
    const {journalEntryForm, journalId } = await req.json();

    const session = await getServerSession(authOptions);
    
    if (!session || !session.user)
        return NextResponse.json({message: "Unauthorized user detected..."}, {status:401});
    
    const id_token = session!.user.id_token;

    try {
      const result = await submitJournalEntry(journalEntryForm, journalId, id_token);
      return NextResponse.json({message: result}, {status:200});
    }
    catch (error){
      const message = "Failed to create journal or fetch journal details. Please try again later."
      return NextResponse.json({message: message}, {status:500});
    }
    
}

export {handler as POST}