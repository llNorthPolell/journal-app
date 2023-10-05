import { NextAuthOptions } from "next-auth";
import NextAuth from "next-auth/next";
import GoogleProvider from "next-auth/providers/google";


export const authOptions : NextAuthOptions = {
    secret: process.env.NEXTAUTH_SECRET,
    providers: [
        GoogleProvider(
            {
                clientId: process.env.GOOGLE_CLIENT_ID!,
                clientSecret: process.env.GOOGLE_CLIENT_SECRET!
            }
        )
    ],
    session: { strategy: "jwt" },
    callbacks:{
        jwt: async ({token,user,account}) =>{
            if (user && account)
                token.access_token=account.access_token;

            return {...token,...user}
        },
        session: async ({session,token})=>{
            session.user=token;
            session.access_token=token.access_token;
            return session;
        },
        redirect: async ({baseUrl}) => {
            return baseUrl;
        }
    }
}
const authHandler = NextAuth(authOptions);

export {authHandler as GET, authHandler as POST}