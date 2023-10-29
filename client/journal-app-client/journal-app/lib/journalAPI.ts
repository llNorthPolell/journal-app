"use server";
import { revalidatePath } from "next/cache";
import { JournalAPIInputFormat } from "../models/journal";
import { GenericResponse } from "../types/generic-response";
import { getIDTokenOrRedirectToLogin } from "./auth";

export const getJournalById = async (journalId: string, id_token: string) : Promise<GenericResponse> => {
    const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + "/journals/" + journalId;
    console.log(`[...journalSeg]/page - ${id_token}`);
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
        message = "[...journalSeg]/page - Error: Unauthorized user detected...";
        console.log(message);
    }

    return {
        apiStatus: res.status,
        contents: contents,
        message: message
    };
}



export const getJournalsByAuthor = async (id_token: string) => {
    const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + "/journals"
    console.log(`[...journalSeg]/page - ${id_token}`);
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
  
    if (res.status == 200) {
      const result = await res.json();
      return result;
    }
    else if (res.status == 401)
      console.log("[...journalSeg]/page - Error: Unauthorized user detected...");
  
    return [];
}


export const submitJournal = async (journal: JournalAPIInputFormat)=>{
  const id_token = await getIDTokenOrRedirectToLogin();

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
      revalidatePath('/');
      return {
          apiStatus: res.status,
          contents: contents,
          message: message
      };
    }
    catch(error){
      console.log(error);
      return {};
    }
}