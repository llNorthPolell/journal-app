import { getServerSession } from "next-auth";
import { authOptions } from "../app/api/auth/[...nextauth]/route";
import { redirect } from 'next/navigation';


export async function getIDTokenOrRedirectToLogin(toUrl? : string){
    const session = await getServerSession(authOptions);
    console.log("lib/auth - getIDTokenOrRedirectToLogin: Time now: " + Date.now());
    
    if (!session || session.user.error == "RefreshAccessTokenError")
        redirect((toUrl)? `/login?callback=${toUrl}`: '/login');

    console.log("lib/auth - getIDTokenOrRedirectToLogin: Session: " + JSON.stringify(session));
    
    return (session.user.id_token);
}