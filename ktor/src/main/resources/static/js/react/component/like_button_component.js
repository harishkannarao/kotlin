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
                }, 'You liked this.'),
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
                'Like'
            )
        ];
    }
}