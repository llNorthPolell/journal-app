import Link from 'next/link';
import {Journal} from '../../models/journal';
import Image from 'next/image';
import { defaultImgUrl } from '../../lib/cloudStorage';

interface JournalCardProps{
    journal:Journal
}

export default function JournalCard(props:JournalCardProps){
    return (
        <Link key={props.journal.id} href={`/${props.journal.id}`} className="journal animated-entry-top btn-link-popout-hover">
            <div className="journal__image-box">
                {
                    (props.journal.img)? 
                        <Image src={props.journal.img}
                            alt={props.journal.name}
                            className="journal__image"
                            width={320}
                            height={480} />
                    :
                    <Image src={defaultImgUrl}
                        alt={props.journal.name}
                        className="journal__image"
                        width={320}
                        height={480} />
                }

            </div>
            <div className="journal__name-box">
                <h1 className="journal__name">
                    {props.journal.name}
                </h1>
            </div>
        </Link>
    )
}