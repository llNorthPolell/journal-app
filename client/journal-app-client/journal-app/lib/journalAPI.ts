import { GenericResponse } from "../types/generic-response";

export const getJournalById = async (journalId: string, id_token: string) : Promise<GenericResponse> => {
    const url = process.env.NEXT_PUBLIC_JOURNAL_APP_API + "/journals/" + journalId;

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
      console.log("Error: Unauthorized user detected...");
  
    return [];
}