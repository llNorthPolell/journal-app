import { GenericResponse } from "../types/generic-response";

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
        message = "[...journalSeg]/page - Error: Unauthorized user detected...";
        console.log(message);
    }

    else {
        message = "[...journalSeg]/page - Error: Failed to get journal entries; " + JSON.stringify(res.text());
        console.log(message);
    }

    return {
        apiStatus: res.status,
        contents: contents,
        message: message
    };
}
