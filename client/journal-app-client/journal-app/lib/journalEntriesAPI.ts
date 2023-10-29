"use server";
import { revalidatePath } from "next/cache";
import { JournalEntryForm } from "../models/journalEntry";
import { GenericResponse } from "../types/generic-response";
import { getIDTokenOrRedirectToLogin } from "./auth";

export const getJournalEntriesInJournal = async (journalId: string, id_token: string) : Promise<GenericResponse> => {
    const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + `/${journalId}/journalEntries`;

    const res = await fetch(
        url,
        {
            method: "GET",
            headers: {
                authorization: `Bearer ${id_token}`,
                "Content-Type": "application/json"
            },
        }
    )

    let contents = undefined;
    let message = "success";

    if (res.status == 200) 
        contents = await res.json();

    else if (res.status == 401){
        message = "lib/journalEntriesAPI - Error: Unauthorized user detected...";
        console.log(message);
    }

    else {
        message = "lib/journalEntriesAPI - Error: Failed to get journal entries; " + JSON.stringify(res.text());
        console.log(message);
    }

    return {
        apiStatus: res.status,
        contents: contents,
        message: message
    };
}



export const submitJournalEntry = async (journalId:string, journalEntry: JournalEntryForm) : Promise<GenericResponse> =>{
    try {
        const id_token = await getIDTokenOrRedirectToLogin(`/${journalId}`);

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

        let contents = undefined;
        let message = "success";
    
        if (res.status == 200) {
            contents = await res.json();
        }
        else if (res.status == 401){
            message = "lib/journalEntriesAPI - Error: Unauthorized user detected...";
            console.log(message);
        }
        else {
            message = "lib/journalEntriesAPI - Error: Failed to create journal entry; " + JSON.stringify(res.text());
            console.log(`res.status: ${res.status}, message: ${message}`);
        }
        revalidatePath(`/${journalId}`);
        return {
            apiStatus: res.status,
            contents: contents,
            message: message
        };
      }
      catch(error){
        console.log(error);
        return {
            apiStatus: 500,
            contents: {},
            message: `lib/journalEntriesAPI - Error: Failed to create journal entry; ${JSON.stringify(error)}`
        };
      }
}