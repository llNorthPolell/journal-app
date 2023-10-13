import { getServerSession } from "next-auth";
import { authOptions } from "../app/api/auth/[...nextauth]/route";
import { redirect } from 'next/navigation';


export async function getIDTokenOrRedirectToLogin(toUrl : string){
    const session = await getServerSession(authOptions);

    if (!session)
        redirect(`/login?callback=${toUrl}`);

    return (session.user.id_token);
}