ReactDOM.render(
    e(
        LikeButton, {
            'preClickMessage': 'Click to \'Like\'',
            'postClickMessage': 'You liked this.'
        },
        []
    ),
    document.querySelector('#like_button_container')
);