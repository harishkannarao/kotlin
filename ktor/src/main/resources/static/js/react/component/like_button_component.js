class LikeButton extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            liked: false
        };
    }

    render() {
        if (this.state.liked) {
            let className = 'qa-liked-message';
            className += ' bold-message';
            return [
                e('span', {
                    'className': className
                }, this.props.postClickMessage),
                ' Awesome !!!'
            ];
        }

        return [
            e('button', {
                    'className': "qa-like-button",
                    onClick: () => this.setState({
                        liked: true
                    })
                },
                this.props.preClickMessage
            )
        ];
    }
}