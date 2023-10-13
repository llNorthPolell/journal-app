import Dashboard from '../../components/dashboard/dashboard';
import { PageProps } from '../../.next/types/app/page';
import { GenericResponse } from '../../types/generic-response';
import { getIDTokenOrRedirectToLogin } from '../../lib/auth';

const getJournal = async (journalId: string, id_token: string) : Promise<GenericResponse> => {
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
        message = "Error: Unauthorized user detected...";
        console.log(message);
    }

    return {
        apiStatus: res.status,
        contents: contents,
        message: message
    };
}

export default async function JournalMain({ params }: PageProps) {
    
    const journalId = params.journalId;
    const id_token = await getIDTokenOrRedirectToLogin(`/${journalId}`);
    const apiResult = await getJournal(journalId,id_token);
    const journal = apiResult.contents;

    if (journal)
        return (
            <Dashboard journal={journal} />
        )

    
    return (     
        <h1>This journal does not belong to you...</h1>
    )
}
