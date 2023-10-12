import type { NextApiRequest } from 'next'
import {NextRequest, NextResponse} from 'next/server'
import { getServerSession } from 'next-auth'
import { authOptions } from '../auth/[...nextauth]/route'
import { JournalAPIInputFormat } from '../../../models/journal'
import {uploadImage, defaultImgUrl} from '../../../lib/cloudStorage';

type JournalAppAPIPostResponse = {
  message: string
}


const submitJournal = async (journal: JournalAPIInputFormat, id_token : string)=>{
    try {
        const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + "/journals"
    
        const res = await fetch(
          url,
          {
            method: "POST",
            headers: {
              authorization: `Bearer ${id_token}`,
              "Content-Type": "application/json"
            },
            body: JSON.stringify(journal)
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
    const journalForm = await req.formData();
    const message=  "Received Form Submission: " + JSON.stringify(journalForm);

    const session = await getServerSession(authOptions);
    
    if (!session || !session.user)
        return NextResponse.json({message: "Unauthorized user detected..."}, {status:401});
    
    
    const id_token = session!.user.id_token;
    const name = journalForm.get('name') as string;
    const image = journalForm.get('img') as Blob;

    let saveImgUrl : string = defaultImgUrl;
    if (image){
      saveImgUrl = await uploadImage(image) as string;
      console.log("New image URL: " + saveImgUrl);
    }

    const journalToSave : JournalAPIInputFormat = {
      name: name,
      img: saveImgUrl
    }

    const result = await submitJournal(journalToSave, id_token);
    
    return NextResponse.json({message: result}, {status:200});

/*
    const journalToSave = {
        name: journalForm.get('name'),
        img: saveImgName
    }

    console.log("Saved journal as " + JSON.stringify(journalToSave));

    return NextResponse.json({message: message}, {status:200});
    /*const submitResult = await submitJournal(journalToSave, id_token);
    if (submitResult)
      return NextResponse.json({message: submitResult}, {status:200});
    
    return NextResponse.json({message: "Failed to create journal or fetch journal details."}, {status:500});*/
}

export {handler as POST}