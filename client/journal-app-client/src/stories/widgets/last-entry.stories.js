import LastEntryWidget from '../../dashboard/widgets/last-entry/last-entry';

export default{
    title: "Widgets/Last Entry Widget",
    component: LastEntryWidget
}

const payload={
    overview: "At vero eos et accusamus et iusto odio\ndignissimos ducimus, qui blanditiis praesentium voluptatum deleniti\natque corrupti, quosdolores et quas molestias excepturi sint, obcaecati\ncupiditate non provident, similique sunt in culpa, qui officia deserunt\nmollitiaanimi, id est laborum et dolorum fuga. Et harum quidem rerum\nfacilis est et expedita distinctio. Nam libero tempore, cum solutanobis\nest eligendi optio, cumque nihil impedit, quo minus id, quod maxime\nplaceat, facere possimus, omnis voluptas assumendaest, omnis dolor repellendus.",
    key: ""
}



export const LastEntryWidgetStory = () => 
    <LastEntryWidget lastEntry={payload}></LastEntryWidget>